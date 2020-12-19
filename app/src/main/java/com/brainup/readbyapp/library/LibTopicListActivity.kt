package com.brainup.readbyapp.library

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.brainup.readbyapp.MainActivity
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.auth.login.UserSelectedSubject
import com.brainup.readbyapp.auth.login.UserSelectedTopics
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.DashBoardActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.HomeViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RateAppRequest
import com.brainup.readbyapp.com.brainup.readbyapp.library.LibTopicListAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.library.LibTopicViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui.InitializQuizActivity
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui.QuizActivityNew
import com.brainup.readbyapp.com.brainup.readbyapp.topics.TopicListActivity
import com.brainup.readbyapp.com.brainup.readbyapp.utils.NewPlayerActivity
import com.brainup.readbyapp.com.brainup.readbyapp.utils.PlayerActivity
import com.brainup.readbyapp.payment.model.Data
import com.brainup.readbyapp.payment.model.InitiatePaymentRequest
import com.brainup.readbyapp.utils.Constants
import com.brainup.readbyapp.utils.PrefrenceData
import com.brainup.readbyapp.utils.ViewDocumentHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import kotlinx.android.synthetic.main.activity_lib_topic_list.*

class LibTopicListActivity : BaseActivity() {

    private lateinit var viewModel: LibTopicViewModel
    private lateinit var dialogBuilder: androidx.appcompat.app.AlertDialog
    private lateinit var dialogBuilderSuccess: androidx.appcompat.app.AlertDialog
    private lateinit var dialogBuilderFailed: androidx.appcompat.app.AlertDialog
    private lateinit var priceText: TextView
    private lateinit var discountText: TextView
    private lateinit var totalPayment: TextView
    private lateinit var purchaseButton: MaterialButton
    private lateinit var headerText: TextView
    private var priceCourse: Double = 0.0
    private lateinit var thanksString: TextView
    private lateinit var txnSuccess: TextView
    private lateinit var retryBtn: MaterialButton
    private lateinit var closeBtn: MaterialButton
    private lateinit var paymentTickAnimation: LottieAnimationView
    private lateinit var paymentFailedAnimation: LottieAnimationView
    private lateinit var failedString: TextView
    private lateinit var txnFailed: TextView

    companion object {
        const val KEY_TITLE = "keyTitle"
        const val KEY_SELECTED_TOPIC = "keySelectedTopic"
        const val KEY_COURSE_ID = "keyCourseId"
        const val KEY_SUBS_ID = "keySubsId"
        const val KEY_COURSE_PRICE = "keyCoursePrice"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib_topic_list)
        initProgressbar(this)
        dialogBuilder = MaterialAlertDialogBuilder(this).create()
        dialogBuilderFailed = MaterialAlertDialogBuilder(this).create()
        dialogBuilderSuccess = MaterialAlertDialogBuilder(this).create()
        viewModel = ViewModelProvider(this).get(LibTopicViewModel::class.java)
        priceCourse = MainActivity.course_price
        title = intent.getStringExtra(TopicListActivity.KEY_TITLE)
        val selectedSubject =
                intent.getSerializableExtra(TopicListActivity.KEY_SELECTED_TOPIC) as UserSelectedChapters
        //   tvStudentName.text = PrefrenceData.getUserName(this)
        rvLibTopics.adapter = LibTopicListAdapter(
                selectedSubject.MAS_TOPICS,
                TopicHandler(this)
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    inner class TopicHandler(val context: Context) {
        fun onItemClick(selectedTopics: UserSelectedTopics) {
           // val videoStatus = PrefrenceData.getVideoStatus(this.context, selectedTopics.TOPIC_ID.toString())
            // if (selectedTopics.isEnable) {

            if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("f")) {
                //if (selectedTopics.isEnable) {
                    //if (selectedTopics.MAS_TOPIC_STATUS == null) {
                        val intent = Intent(context, NewPlayerActivity::class.java)
                        intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
                        intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
                        intent.putExtra(PlayerActivity.TOPIC_ID, "" + selectedTopics.TOPIC_ID)
                        intent.putExtra(PlayerActivity.USER_SUBS, "" + selectedTopics.TOPIC_SUBSCRIPTION)
                        intent.putExtra("IS_FROM_LIBRARY", "Y")
                        if (selectedTopics.MAS_TOPIC_STATUS != null) {
                            intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + selectedTopics.MAS_TOPIC_STATUS.TOPIC_STATUS_ID)
                            intent.putExtra(PlayerActivity.TEST_STATUS, selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS)
                        } else {
                            intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + 0)
                            intent.putExtra(PlayerActivity.TEST_STATUS, "")
                        }
                        context.startActivity(intent)
//                    } else {
//                        Toast.makeText(
//                                this@LibTopicListActivity,
//                                "you have already  seen  this  topic video ",
//                                Toast.LENGTH_LONG
//                        ).show()
//                    }

               // }
//                if (selectedTopics.MAS_TOPIC_STATUS != null
//                        && (selectedTopics.MAS_TOPIC_STATUS.VIDEO_STATUS.contentEquals("p") || videoStatus.contentEquals("p"))) {
//                    val intent = Intent(context, NewPlayerActivity::class.java)
//                    intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
//                    intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
//                    intent.putExtra(PlayerActivity.TOPIC_ID, "" + selectedTopics.TOPIC_ID)
//                    intent.putExtra(PlayerActivity.USER_SUBS, "" + selectedTopics.TOPIC_SUBSCRIPTION)
//                    if (selectedTopics.MAS_TOPIC_STATUS != null) {
//                        intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + selectedTopics.MAS_TOPIC_STATUS.TOPIC_STATUS_ID)
//                        intent.putExtra(PlayerActivity.TEST_STATUS, selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS)
//                    } else {
//                        intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + 0)
//                        intent.putExtra(PlayerActivity.TEST_STATUS, "")
//                    }
//                    context.startActivity(intent)
//                } else if (selectedTopics.MAS_TOPIC_STATUS != null
//                        && (selectedTopics.MAS_TOPIC_STATUS.VIDEO_STATUS.contentEquals("c") || videoStatus.contentEquals("c"))) {
//
//                    val intent = Intent(context, NewPlayerActivity::class.java)
//                    intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
//                    intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
//                    intent.putExtra(PlayerActivity.TOPIC_ID, "" + selectedTopics.TOPIC_ID)
//                    intent.putExtra(PlayerActivity.USER_SUBS, "" + selectedTopics.TOPIC_SUBSCRIPTION)
//                    if (selectedTopics.MAS_TOPIC_STATUS != null) {
//                        intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + selectedTopics.MAS_TOPIC_STATUS.TOPIC_STATUS_ID)
//                        intent.putExtra(PlayerActivity.TEST_STATUS, selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS)
//                    } else {
//                        intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + 0)
//                        intent.putExtra(PlayerActivity.TEST_STATUS, "")
//                    }
//                    context.startActivity(intent)
//
//                } else {
//                    Toast.makeText(
//                            this@LibTopicListActivity,
//                            "Please complete above topics/Videos before this topic.",
//                            Toast.LENGTH_LONG
//                    ).show()
//                }
            }
            else if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("p")) {

                if (MainActivity.subscription_flag.contentEquals("p")) {

                //    if (selectedTopics.isEnable) {
                  //      if (selectedTopics.MAS_TOPIC_STATUS == null) {
                            val intent = Intent(context, NewPlayerActivity::class.java)
                            intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
                            intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
                            intent.putExtra(PlayerActivity.TOPIC_ID, "" + selectedTopics.TOPIC_ID)
                            intent.putExtra(PlayerActivity.USER_SUBS, "" + selectedTopics.TOPIC_SUBSCRIPTION)
                            intent.putExtra("IS_FROM_LIBRARY", "Y")
                            if (selectedTopics.MAS_TOPIC_STATUS != null) {
                                intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + selectedTopics.MAS_TOPIC_STATUS.TOPIC_STATUS_ID)
                                intent.putExtra(PlayerActivity.TEST_STATUS, selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS)
                            } else {
                                intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + 0)
                                intent.putExtra(PlayerActivity.TEST_STATUS, "")
                            }
                            context.startActivity(intent)
//                        } else {
//                            Toast.makeText(
//                                    this@LibTopicListActivity,
//                                    "you have already  seen  this  topic video ",
//                                    Toast.LENGTH_LONG
//                            ).show()
//                        }
//
//                    }
//                    if (selectedTopics.MAS_TOPIC_STATUS != null
//                            && (selectedTopics.MAS_TOPIC_STATUS.VIDEO_STATUS.contentEquals("p") || videoStatus.contentEquals("p"))) {
//                        val intent = Intent(context, NewPlayerActivity::class.java)
//                        intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
//                        intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
//                        intent.putExtra(PlayerActivity.TOPIC_ID, "" + selectedTopics.TOPIC_ID)
//                        intent.putExtra(PlayerActivity.USER_SUBS, "" + selectedTopics.TOPIC_SUBSCRIPTION)
//                        if (selectedTopics.MAS_TOPIC_STATUS != null) {
//                            intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + selectedTopics.MAS_TOPIC_STATUS.TOPIC_STATUS_ID)
//                            intent.putExtra(PlayerActivity.TEST_STATUS, selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS)
//                        } else {
//                            intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + 0)
//                            intent.putExtra(PlayerActivity.TEST_STATUS, "")
//                        }
//                        context.startActivity(intent)
//                    } else if (selectedTopics.MAS_TOPIC_STATUS != null
//                            && (selectedTopics.MAS_TOPIC_STATUS.VIDEO_STATUS.contentEquals("c") || videoStatus.contentEquals("c"))) {
//
//                        val intent = Intent(context, NewPlayerActivity::class.java)
//                        intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
//                        intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
//                        intent.putExtra(PlayerActivity.TOPIC_ID, "" + selectedTopics.TOPIC_ID)
//                        intent.putExtra(PlayerActivity.USER_SUBS, "" + selectedTopics.TOPIC_SUBSCRIPTION)
//                        if (selectedTopics.MAS_TOPIC_STATUS != null) {
//                            intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + selectedTopics.MAS_TOPIC_STATUS.TOPIC_STATUS_ID)
//                            intent.putExtra(PlayerActivity.TEST_STATUS, selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS)
//                        } else {
//                            intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + 0)
//                            intent.putExtra(PlayerActivity.TEST_STATUS, "")
//                        }
//                        context.startActivity(intent)
//                    } else {
//                        Toast.makeText(
//                                this@LibTopicListActivity,
//                                "Please complete above topics/Videos before this topic.",
//                                Toast.LENGTH_LONG
//                        ).show()
//                    }
                }

                else if (MainActivity.subscription_flag.contentEquals("u")) {

                    if (MainActivity.course_price.toInt() > 0) {
                        showDialog()
                    } else {
                        Toast.makeText(
                                this@LibTopicListActivity,
                                "Price for payment must be greater than 0.",
                                Toast.LENGTH_LONG
                        ).show()
                    }


                }
            }

        }

        fun onPdfButtonClick(selectedTopics: UserSelectedTopics) {
            if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("f")) {

                if (selectedTopics.BOOK_URL != null) {
                    ViewDocumentHelper.navigateToCustomTab(context, selectedTopics.BOOK_URL)
                }

            } else if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("p")) {

                if (MainActivity.subscription_flag.contentEquals("p")) {

                    if (selectedTopics.BOOK_URL != null) {
                        ViewDocumentHelper.navigateToCustomTab(context, selectedTopics.BOOK_URL)
                    }
                } else if (MainActivity.subscription_flag.contentEquals("u")) {

                    if (MainActivity.course_price.toInt() > 0) {
                        showDialog()
                    } else {
                        Toast.makeText(
                                this@LibTopicListActivity,
                                "Price for payment must be greater than 0.",
                                Toast.LENGTH_LONG
                        ).show()
                    }


                }
            }
        }

//        fun onCardClick(selectedTopics: UserSelectedTopics) {
//            val videoStatus = PrefrenceData.getVideoStatus(this.context, selectedTopics.TOPIC_ID.toString());
//            MainActivity.topic_id = selectedTopics.TOPIC_ID.toString()
//            if (selectedTopics.TOPIC_ID != null) {
//
//                if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("f")) {
//                    if (selectedTopics.MAS_TOPIC_STATUS != null) {
//                        if ((selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("") || selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("p"))
//                                && (selectedTopics.VIDEO_STATUS.contentEquals("c") || videoStatus.contentEquals("c"))) {
//                            val intent = Intent(this@LibTopicListActivity, InitializQuizActivity::class.java)
//                            intent.putExtra(QuizActivityNew.KEY_SELECTED_TOPIC, selectedTopics)
//                            startActivity(intent)
//                        } else if (selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("c")
//                                && (selectedTopics.MAS_TOPIC_STATUS.VIDEO_STATUS.contentEquals("c") || videoStatus.contentEquals("c"))) {
//                            val intent = Intent(this@LibTopicListActivity, InitializQuizActivity::class.java)
//                            intent.putExtra(QuizActivityNew.KEY_SELECTED_TOPIC, selectedTopics)
//                            startActivity(intent)
//                            // Toast.makeText(this@TopicListActivity,"You have  already done video and test ",Toast.LENGTH_LONG).show()
//
//                        } else {
//                            Toast.makeText(
//                                    this@LibTopicListActivity,
//                                    "Please complete video before test.",
//                                    Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    } else {
//                        Toast.makeText(
//                                this@LibTopicListActivity,
//                                "Please complete video before test.",
//                                Toast.LENGTH_LONG
//                        ).show()
//                    }
//                } else if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("p")) {
//
//                    if (MainActivity.subscription_flag.contentEquals("p")) {
//                        if (selectedTopics.MAS_TOPIC_STATUS != null) {
//                            if ((selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("") || selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("p"))
//                                    && (selectedTopics.MAS_TOPIC_STATUS.VIDEO_STATUS.contentEquals("c") || videoStatus.contentEquals("c"))) {
//                                val intent = Intent(this@LibTopicListActivity, InitializQuizActivity::class.java)
//                                intent.putExtra(QuizActivityNew.KEY_SELECTED_TOPIC, selectedTopics)
//                                startActivity(intent)
//                            } else if (selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("c")
//                                    && (selectedTopics.MAS_TOPIC_STATUS.VIDEO_STATUS.contentEquals("c") || videoStatus.contentEquals("c"))) {
//                                val intent = Intent(this@LibTopicListActivity, InitializQuizActivity::class.java)
//                                intent.putExtra(QuizActivityNew.KEY_SELECTED_TOPIC, selectedTopics)
//                                startActivity(intent)
//                                // Toast.makeText(this@TopicListActivity,"You have  already done video and test ",Toast.LENGTH_LONG).show()
//
//                            } else {
//                                Toast.makeText(
//                                        this@LibTopicListActivity,
//                                        "Please complete video before test.",
//                                        Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        } else {
//                            Toast.makeText(
//                                    this@LibTopicListActivity,
//                                    "Please complete video before test.",
//                                    Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    } else if (MainActivity.subscription_flag.contentEquals("u")) {
//
//                        if (MainActivity.course_price.toInt() > 0) {
//                            showDialog()
//                        } else {
//
//                            Toast.makeText(
//                                    this@LibTopicListActivity,
//                                    "Price for payment must be greater than 0.",
//                                    Toast.LENGTH_LONG
//                            ).show()
//                        }
//
//
//                    }
//                }
//            } else {
//
//                Toast.makeText(
//                        this@LibTopicListActivity,
//                        "Please complete video before test.",
//                        Toast.LENGTH_LONG
//                ).show()
//            }
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            // onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onPayClick() {
        getInitiatePayment()
    }

    private fun getInitiatePayment() {
        progressDialog.show()
        var userid = PrefrenceData.getUserId(this)
        var model = InitiatePaymentRequest(

                "1".toInt(),
                "TXN_PENDING",
                MainActivity.course_price,
                userid.toInt(),
                MainActivity.course_id
        )
        viewModel.getInitiatePayment(model)?.observe(this@LibTopicListActivity, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
                val body = it.body
                var list = body?.data
                onStartTransaction(list)
            }
        })

    }

    private fun onStartTransaction(rsp: Data?) {
        var userid = PrefrenceData.getUserId(this)
        val Service = PaytmPGService.getStagingService("https://securegw-stage.paytm.in/order/process")
        val paramMap = HashMap<String, String>()
        var mid = "oBKImO82025244498334"
        paramMap.put("MID", mid)
        paramMap.put("CUST_ID", "CUST_" + userid)
        paramMap.put("ORDER_ID", "" + rsp?.ORDER_ID)
        paramMap.put("INDUSTRY_TYPE_ID", "Retail")
        paramMap.put("CHANNEL_ID", "WAP")
        paramMap.put("TXN_AMOUNT", MainActivity.course_price.toString())
        paramMap.put("WEBSITE", "APPSTAGING")
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + rsp?.ORDER_ID)
        paramMap.put("CHECKSUMHASH", rsp!!.CHECKSUM_VAL);
        val Order = PaytmOrder(paramMap)
        Service.initialize(Order, null)
        Service.startPaymentTransaction(this, true, true,
                object : PaytmPaymentTransactionCallback {
                    override fun someUIErrorOccurred(inErrorMessage: String) {
                        showFailedPaymentDialog()
                    }
                    override fun onTransactionResponse(inResponse: Bundle) {
                        if (inResponse.get("STATUS").toString().contentEquals("TXN_SUCCESS")) {
                            val paymentStatus =
                                    Data(
                                            USER_TRANS_ID = rsp.USER_TRANS_ID,
                                            USER_ID = rsp.USER_ID,
                                            ORDER_ID = rsp.ORDER_ID,
                                            COURSE_ID = rsp.COURSE_ID,
                                            SUBJECT_ID = rsp.SUBJECT_ID,
                                            PAYMENT_STATUS = inResponse.get("STATUS") as String,
                                            TRANSACTION_AMOUNT = rsp.TRANSACTION_AMOUNT,
                                            CHECKSUM_VAL = inResponse.get("CHECKSUMHASH") as String,
                                            BANKNAME = inResponse.get("BANKNAME") as String,
                                            MID = mid,
                                            TXNID = inResponse.get("TXNID") as String,
                                            RESPCODE = inResponse.get("RESPCODE") as String,
                                            PAYMENTMODE = inResponse.get("PAYMENTMODE") as String,
                                            BANKTXNID = inResponse.get("BANKTXNID") as String,
                                            GATEWAYNAME = inResponse.get("GATEWAYNAME") as String,
                                            CURRENCY = inResponse.get("CURRENCY") as String,
                                            SUBSCRIPTION_ID = MainActivity.subs_id,
                                            TXNDATE = ""
                                    )

                            viewModel.sendTxnStatusDetails(paymentStatus)
                                    ?.observe(this@LibTopicListActivity, Observer {
                                        progressDialog.dismiss()
                                        if (it.isSuccessful) {
                                            val body = it.body
                                            var list = body?.data
                                            dialogBuilder.dismiss()
                                            Toast.makeText(
                                                    this@LibTopicListActivity,
                                                    "Payment Updated Successfully",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                            showSuccessPaymentDialog()

                                        }
                                    })
                        } else if (inResponse.get("STATUS").toString().contentEquals("TXN_FAILURE")) {
                            showFailedPaymentDialog()
                        }


                    }

                    override fun networkNotAvailable() { // If network is not
                        Toast.makeText(this@LibTopicListActivity, "Payment Transaction response networkNotAvailable", Toast.LENGTH_LONG).show();
                        showFailedPaymentDialog()
                    }

                    override fun onErrorProceed(p0: String?) {
                        showFailedPaymentDialog()
                    }

                    override fun clientAuthenticationFailed(inErrorMessage: String) {
                        showFailedPaymentDialog()
                    }

                    override fun onErrorLoadingWebPage(
                            iniErrorCode: Int,
                            inErrorMessage: String,
                            inFailingUrl: String
                    ) {
                        showFailedPaymentDialog()
                    }

                    override fun onBackPressedCancelTransaction() {
                        showFailedPaymentDialog()
                    }

                    override fun onTransactionCancel(
                            inErrorMessage: String,
                            inResponse: Bundle
                    ) {
                        showFailedPaymentDialog()
                    }
                })
    }


    private fun showDialog() {
        val inflater = this@LibTopicListActivity.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.subscription_purchase_dialog, null)
        headerText = dialogView.findViewById<View>(R.id.header_text) as TextView
        priceText = dialogView.findViewById<View>(R.id.text_string) as TextView
        discountText = dialogView.findViewById<View>(R.id.discount_text) as TextView
        totalPayment = dialogView.findViewById<View>(R.id.total_payment_text) as TextView
        purchaseButton = dialogView.findViewById<View>(R.id.purchase_btn) as MaterialButton

        priceText.text = getString(R.string.text_string_payment) + " " + MainActivity.course_price
        discountText.text = getString(R.string.discount_text) + " " + 0.0
        totalPayment.text = getString(R.string.total_payment_text) + " " + MainActivity.course_price

        headerText.setMovementMethod(LinkMovementMethod.getInstance())
        headerText.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse("https://www.readby.co.in/refundpolicy.html")
            startActivity(browserIntent)
        })
        purchaseButton.setOnClickListener {

            progressDialog.show()
            dialogBuilder.dismiss()
            onPayClick()

        }

        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    private fun showSuccessPaymentDialog() {
        val inflater = this@LibTopicListActivity.layoutInflater
        val dialogViewSuccess: View = inflater.inflate(R.layout.payment_success_dialog, null)
        headerText = dialogViewSuccess.findViewById<View>(R.id.header_text) as TextView
        thanksString = dialogViewSuccess.findViewById<View>(R.id.thanks_string) as TextView
        txnSuccess = dialogViewSuccess.findViewById<View>(R.id.txn_success) as TextView
        closeBtn = dialogViewSuccess.findViewById<View>(R.id.close_btn) as MaterialButton
        paymentTickAnimation =
                dialogViewSuccess.findViewById<View>(R.id.animationView) as LottieAnimationView
        closeBtn.setOnClickListener {

            dialogBuilder.dismiss()
            val intent = Intent(this@LibTopicListActivity, DashBoardActivity::class.java)
            this@LibTopicListActivity.startActivity(intent)

        }

        dialogBuilderSuccess.setView(dialogViewSuccess)
        dialogBuilderSuccess.show()
    }

    private fun showFailedPaymentDialog() {
        val inflater = this@LibTopicListActivity.layoutInflater
        val dialogViewFailed: View = inflater.inflate(R.layout.payment_failed_dialog, null)
        headerText = dialogViewFailed.findViewById<View>(R.id.header_text) as TextView
        failedString = dialogViewFailed.findViewById<View>(R.id.failed_string) as TextView
        txnFailed = dialogViewFailed.findViewById<View>(R.id.txn_failed) as TextView
        retryBtn = dialogViewFailed.findViewById<View>(R.id.retry_btn) as MaterialButton
        paymentFailedAnimation =
                dialogViewFailed.findViewById<View>(R.id.animation_View_failed) as LottieAnimationView
        retryBtn.setOnClickListener {
            dialogBuilderFailed.dismiss()
            showDialog()
        }
        dialogBuilderFailed.setView(dialogViewFailed)
        dialogBuilderFailed.show()
    }
}