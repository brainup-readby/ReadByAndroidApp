package com.brainup.readbyapp.dashboard

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.MainActivity.Companion.subject_id
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.auth.login.UserSelectedSubject
import com.brainup.readbyapp.chapter.ChapterListActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.HomeViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.adapter.SubjectListAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.library.CategoryParentAdapter
import com.brainup.readbyapp.library.LibTopicListActivity
import com.brainup.readbyapp.payment.model.Data
import com.brainup.readbyapp.payment.model.InitiatePaymentRequest
import com.brainup.readbyapp.utils.PrefrenceData
import com.mj.elearning24.base.BaseFragment
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_dash_board_acitivty.*
import java.util.*
import kotlin.collections.HashMap


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: HomeViewModel
    private var userData: UserData? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this.requireActivity()).get(HomeViewModel::class.java)
        initProgressbar(this.requireContext())
        getUserDetails()
        handleToggle()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getUserDetails() {
          progressDialog.show()
          viewModel.getUserDetails(PrefrenceData.getUserId(this.requireContext()))
              ?.observe(Home@ this, Observer {
                  progressDialog.dismiss()
                  if (it.isSuccessful) {
                      val body = it.body
                      userData = body?.data
                      setData()
                      //Toast.makeText(this, body?.data?.USER_ID.toString(), Toast.LENGTH_SHORT).show()
                  }
              })
    /*    val dashBoardActivity = getActivity() as DashBoardActivity
        dashBoardActivity.viewModel.userData?.observe(dashBoardActivity, Observer {
            userData = it
            setData()
        })
        userData = dashBoardActivity.viewModel.userData?.value
        if (userData != null)
            setData()*/
    }

    private fun setData() {
        val name = userData?.FIRST_NAME
        getActivity()?.tvHeaderTitle?.text = name
        getActivity()?.tvClass?.text = userData?.USER_SUBSCRIPTION?.get(0)?.MAS_COURSE?.COURSE_NAME

       setPrefsValues(userData)

        tvStudentName.text =  "Hi" +" "+name

        showGreeting()

        /*rvSubjects.setLayoutManager(
            LinearLayoutManager(
                this.requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        )*/
        rvSubjects.adapter =
            SubjectListAdapter(
                userData?.USER_SUBSCRIPTION?.get(0)?.MAS_STREAM?.MAS_SUBJECT,
                SubjectClickHandler(this.requireContext())
            )
        val subjectListAdapter = CategoryParentAdapter()
        subjectListAdapter.submitList(userData?.USER_SUBSCRIPTION?.get(0)?.MAS_STREAM?.MAS_SUBJECT)
        rvLib.adapter = subjectListAdapter


        shareApps.setOnClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val shareSubText = "READBY - The Great Learning app"
            val shareBodyText =  "http://play.google.com/store/apps/details?id=com.brainup.readbyapp"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText)
            startActivity(Intent.createChooser(shareIntent, "Share With"))
        }
    }

    private fun showGreeting() {
        val date = Date()
        val cal: Calendar = Calendar.getInstance()
        cal.setTime(date)
        val hour: Int = cal.get(Calendar.HOUR_OF_DAY)

       var  greeting :  String
        if(hour>= 12 && hour < 17){
            greeting = "Good Afternoon";
        } else if(hour >= 17 && hour < 21){
            greeting = "Good Evening";
        } else if(hour >= 21 && hour < 24){
            greeting = "Good Night";
        } else {
            greeting = "Good Morning";
        }

        tvGreetingMsg.text = greeting
        tvGreetingMsg.visibility = View.VISIBLE

    }

    private fun setPrefsValues(userData: UserData?) {
        val fname = userData?.FIRST_NAME
        val lname = userData?.LAST_NAME
        val email = userData?.EMAIL_ID
        val mobile = userData?.MOBILE_NO
        if(fname!=null  && lname!=null){
            PrefrenceData.setUserName(this.requireContext(), fname.toString()+" "+lname.toString())
        }else {
            PrefrenceData.setUserName(this.requireContext(), fname.toString())
        }
        PrefrenceData.setEmailID(this.requireContext(), email.toString())
        PrefrenceData.setMobNo(this.requireContext(), mobile.toString())
    }

    private fun handleToggle() {
        btnToggle.setOnClickListener {
            if (btnToggle.text == getString(R.string.library)) {
                personalizedHandler()
                btnToggle.text = getString(R.string.personalised)
                val img: Drawable = this.requireContext().getDrawable(R.drawable.ic_location)!!
                btnToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
            } else {
                btnToggle.text = getString(R.string.library)
                libHandler()
                val img: Drawable = this.requireContext().getDrawable(R.drawable.ic_playlist)!!
                btnToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
            }
        }
    }

    private fun personalizedHandler() {
        rvLib.visibility = View.GONE
        rvSubjects.visibility = View.VISIBLE
        // Toast.makeText(this.requireContext(), "personalizedHandler", Toast.LENGTH_SHORT).show()
    }

    private fun libHandler() {
        rvLib.visibility = View.VISIBLE
        rvSubjects.visibility = View.GONE
        //Toast.makeText(this.requireContext(), "libHandler", Toast.LENGTH_SHORT).show()
    }

    inner class SubjectClickHandler(val context: Context) {
        fun onItemClick(userSelectedSubject: UserSelectedSubject) {
            subject_id = userSelectedSubject.SUBJECT_ID.toString()
            val intent = Intent(context, ChapterListActivity::class.java)
            intent.putExtra(ChapterListActivity.KEY_TITLE, userSelectedSubject.SUBJECT_NAME)
            intent.putExtra(ChapterListActivity.KEY_SELECTED_SUBJECT, userSelectedSubject)
            context.startActivity(intent)
            //Toast.makeText(context, vendorDealModel.title + "clicked", Toast.LENGTH_SHORT).show()
        }

        fun onPayClick(userSelectedSubject: UserSelectedSubject){
           // Toast.makeText(getActivity(),"clla",Toast.LENGTH_SHORT).show()
            getInitiatePayment(userSelectedSubject)
        }
    }

    class ChapterClickHandler(val context: Context) {
        fun onItemClick(userSelectedChapters: UserSelectedChapters) {
            val intent = Intent(context, LibTopicListActivity::class.java)
            intent.putExtra(LibTopicListActivity.KEY_TITLE, userSelectedChapters.CHAPTER_NAME)
            intent.putExtra(LibTopicListActivity.KEY_SELECTED_TOPIC, userSelectedChapters)
            context.startActivity(intent)
            //Toast.makeText(context,userSelectedChapters.CHAPTER_NAME,Toast.LENGTH_SHORT).show()
        }
    }






    fun  getInitiatePayment(userSelectedSubject: UserSelectedSubject) {
        progressDialog.show()
        var   userid   = PrefrenceData.getUserId(this.requireContext())
        var  model =  InitiatePaymentRequest(
            userSelectedSubject.STREAM_ID,"P",userSelectedSubject.SUBJECT_PRICE.toInt(),userid.toInt()
        )
        viewModel.getInitiatePayment(model) ?.observe(QuizActivityNew@ this, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
                val body = it.body
                var list  =   body?.data
                onStartTransaction(list)
            }
        })

    }

    fun onStartTransaction(rsp: Data?) {
        val Service = PaytmPGService.getStagingService()
        val paramMap = HashMap<String, String>()
        var mid  ="oBKImO82025244498334"
      //  var mid  ="tNLGFl82486475987971"



        paramMap.put("CALLBACK_URL","https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+rsp?.ORDER_ID)
        //paramMap.put("CALLBACK_URL","https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+rsp?.ORDER_ID)
        paramMap.put("CHECKSUMHASH", rsp!!.CHECKSUM_VAL)
        paramMap.put("CUST_ID",  "CUST_" +rsp?.ORDER_ID)
        paramMap.put("MID",mid)
        paramMap.put("ORDER_ID",""+rsp?.ORDER_ID)
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", ""+rsp?.TRANSACTION_AMOUNT + ".00");
       // paramMap.put("MOBILE_NO", "9587666665");
        paramMap.put("WEBSITE", "APPSTAGING");


    /*   // PAYTM_MID=SMARTE39984924194388


       // var mid  ="SMARTE39984924194388"
        var mid  ="oBKImO82025244498334"
        paramMap.put("CALLBACK_URL","https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+"paytm_bcxBYECoNWDPJ7m")
      //  paramMap.put("CHECKSUMHASH","S7lEvf3G38zHspWl62RV5y/eVABX/kEPPoqMmtodRp0HxkGilT2EOO7SFc4WlqlFX9ecspDKGGZqTqHh5axaGaAlsW7Jkr9ioIcqobs/OX0=")
        paramMap.put("CHECKSUMHASH", rsp!!.CHECKSUM_VAL)
        paramMap.put("CUST_ID",  "CUST_" +"9587666665")
        paramMap.put("MID",mid)
        paramMap.put("ORDER_ID","paytm_bcxBYECoNWDPJ7m")
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", "20"+ ".00");
       // paramMap.put("MOBILE_NO", "9587666665");
      //  paramMap.put("WEBSITE", "APPPROD");
        paramMap.put("WEBSITE", "APPSTAGING");

*/

        val Order = PaytmOrder(paramMap)
        Service.initialize(Order, null)
        Service.startPaymentTransaction(this.requireContext(), true, true,
            object : PaytmPaymentTransactionCallback {
                override fun someUIErrorOccurred(inErrorMessage: String) {}
                override fun onTransactionResponse(inResponse: Bundle) {
                    Log.d("LOG", "Payment Transaction is successful $inResponse")
                    //  Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                }

                override fun networkNotAvailable() { // If network is not

                    Log.d("LOG", "Payment Transaction is networkNotAvailable " + "")
                    // available, then this
                    // method gets called.
                }



                override fun clientAuthenticationFailed(inErrorMessage: String) {
                    Log.d("LOG", "Payment Transaction is networkNotAvailable " + "")
                }

                override fun onErrorLoadingWebPage(
                    iniErrorCode: Int,
                    inErrorMessage: String,
                    inFailingUrl: String
                ) {
                    Log.d("LOG", "Payment Transaction is networkNotAvailable " + "")
                }

                override fun onBackPressedCancelTransaction() {
                    // Toast.makeText(PaymentActiviyForSalectDevice.this,"Back pressed. Transaction cancelled",Toast.LENGTH_LONG).show();
                }

                override fun onTransactionCancel(
                    inErrorMessage: String,
                    inResponse: Bundle
                ) {
                    Log.d("LOG", "Payment Transaction Failed $inErrorMessage")
                    //  Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                }
            })
    }





}