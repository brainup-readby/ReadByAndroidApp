package com.brainup.readbyapp.com.brainup.readbyapp.topics

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.brainup.readbyapp.MainActivity
import com.brainup.readbyapp.MainActivity.Companion.chapter_id
import com.brainup.readbyapp.MainActivity.Companion.subject_id
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.auth.login.UserSelectedTopics
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.DashBoardActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.HomeViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui.InitializQuizActivity
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui.QuizActivityNew
import com.brainup.readbyapp.com.brainup.readbyapp.utils.NewPlayerActivity
import com.brainup.readbyapp.com.brainup.readbyapp.utils.PlayerActivity
import com.brainup.readbyapp.payment.model.Data
import com.brainup.readbyapp.payment.model.InitiatePaymentRequest
import com.brainup.readbyapp.utils.PrefrenceData
import com.brainup.readbyapp.utils.ViewDocumentHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import kotlinx.android.synthetic.main.activity_topic_list.*


class TopicListActivity : BaseActivity(), CellClickListener {

    private var selectedSubject: UserSelectedChapters? = null
    private lateinit var dialogBuilder: androidx.appcompat.app.AlertDialog
    private lateinit var dialogBuilderSuccess: androidx.appcompat.app.AlertDialog
    private lateinit var dialogBuilderFailed: androidx.appcompat.app.AlertDialog
    private lateinit var priceText: TextView
    private lateinit var discountText: TextView
    private lateinit var totalPayment: TextView
    private lateinit var headerText: TextView
    private lateinit var thanksString: TextView
    private lateinit var txnSuccess: TextView
    private lateinit var purchaseButton: MaterialButton
    private lateinit var closeBtn: MaterialButton
    private lateinit var retryBtn: MaterialButton
    private var priceCourse: Double = 0.0
    private lateinit var paymentFailedAnimation: LottieAnimationView
    private lateinit var failedString: TextView
    private lateinit var txnFailed: TextView
    private lateinit var paymentTickAnimation: LottieAnimationView

    companion object {
        const val KEY_TITLE = "keyTitle"
        const val KEY_SELECTED_TOPIC = "keySelectedTopic"
    }

    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_list)
        dialogBuilder = MaterialAlertDialogBuilder(this).create()
        dialogBuilderFailed = MaterialAlertDialogBuilder(this).create()
        dialogBuilderSuccess = MaterialAlertDialogBuilder(this).create()
        priceCourse = MainActivity.course_price
        title = intent.getStringExtra(KEY_TITLE)
        if (intent.getSerializableExtra(KEY_SELECTED_TOPIC) != null
                && MainActivity.is_From_Profile.contentEquals("Y")) {
            selectedSubject =
                    intent.getSerializableExtra(KEY_SELECTED_TOPIC) as UserSelectedChapters
        }
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        tvStudentName.text = "Hi " + PrefrenceData.getUserName(this)
        //rvTopicList.adapter = TopicListAdapter(selectedSubject.MAS_TOPICS, TopicHandler(this))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        initProgressbar(this)
    }

    override fun onResume() {
        super.onResume()
        if (selectedSubject != null && MainActivity.is_From_Profile.contentEquals("Y")) {
            if (MainActivity.for_First_time.contentEquals("Y")) {
                getSelectedChaptersFromProfile()
            } else if (MainActivity.for_First_time.contentEquals("N")) {
                refreshSubscriptionTopicList()
            }
        } else if (MainActivity.is_From_Profile.contentEquals("N")) {
            getUserDetails()
        }
    }

    private fun getSelectedChaptersFromProfile() {
        val list = setDataFromBundle(selectedSubject)
        var listEnable = getEnableDesableObjectList(list)
        rvTopicList.adapter = TopicListAdapter(listEnable, this, this)
    }

    private fun getUserDetails() {
        progressDialog.show()
        viewModel.getUserDetails(PrefrenceData.getUserId(this))
                ?.observe(TopicListActivity@ this, Observer {
                    progressDialog.dismiss()
                    if (it.isSuccessful) {
                        val body = it.body
                        var userData = body?.data
                        val list = setData(userData)
                        var listEnable = getEnableDesableObjectList(list)
                        rvTopicList.adapter = TopicListAdapter(listEnable, this, this)
                    }
                })
    }

    private fun refreshSubscriptionTopicList() {
        progressDialog.show()
        viewModel.getUserDetails(PrefrenceData.getUserId(this))
                ?.observe(TopicListActivity@ this, Observer {
                    progressDialog.dismiss()
                    if (it.isSuccessful) {
                        val body = it.body
                        var userData = body?.data
                        val list = setDataRefreshSubscriptionList(userData)
                        var listEnable = getEnableDesableObjectList(list)
                        rvTopicList.adapter = TopicListAdapter(listEnable, this, this)
                    }
                })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getEnableDesableObjectList(userData: List<UserSelectedTopics>?): List<UserSelectedTopics>? {
        for (item in userData!!) {
            if (item.MAS_TOPIC_STATUS == null) {
                item.isEnable = true
                return userData
            }
            if (item.MAS_TOPIC_STATUS != null && item.MAS_TOPIC_STATUS.VIDEO_STATUS.equals("p")) {
                item.isEnable = true
                return userData
            }
            if (item.MAS_TOPIC_STATUS != null && item.MAS_TOPIC_STATUS.VIDEO_STATUS.equals("c")) {
                item.isEnable = true
                return userData
            }
        }
        return userData
    }


    private fun setData(
            userData: UserData?
    ): List<UserSelectedTopics>? {
        var list = arrayListOf<UserSelectedTopics>()
        if (userData != null){
        if (userData!!.USER_SUBSCRIPTION[userData!!.USER_SUBSCRIPTION.size - 1].MAS_STREAM != null) {
            var streemModel = userData!!.USER_SUBSCRIPTION[userData!!.USER_SUBSCRIPTION.size - 1].MAS_STREAM
            var listOfsubject = streemModel.MAS_SUBJECT
            for (item in listOfsubject) {
                var id = item.SUBJECT_ID
                if (id == subject_id.toInt()) {
                    var listofchapter = item.MAS_CHAPTERS
                    for (chapterItem in listofchapter) {
                        var id = chapterItem.CHAPTER_ID
                        if (id == chapter_id.toInt()) {
                            var listofTopic = chapterItem.MAS_TOPICS
                            list.addAll(listofTopic)
                        }
                    }
                }
            }
        }
    }
        return list

    }

    private fun setDataRefreshSubscriptionList(
            userData: UserData?
    ): List<UserSelectedTopics>? {
        var list = arrayListOf<UserSelectedTopics>()
        if (userData != null) {
            for (subscriptionData in userData.USER_SUBSCRIPTION) {
                if (subscriptionData.SUBSCRIPTION_ID == MainActivity.subs_id) {
                    var streemModel = subscriptionData.MAS_STREAM
                    var listOfsubject = streemModel.MAS_SUBJECT
                    for (item in listOfsubject) {
                        var id = item.SUBJECT_ID
                        if (id == subject_id.toInt()) {
                            var listofchapter = item.MAS_CHAPTERS
                            for (chapterItem in listofchapter) {
                                var id = chapterItem.CHAPTER_ID
                                if (id == chapter_id.toInt()) {
                                    var listofTopic = chapterItem.MAS_TOPICS
                                    list.addAll(listofTopic)
                                }
                            }
                        }
                    }
                }
            }
        }
        return list
    }

    private fun setDataFromBundle(
            selectedChapter: UserSelectedChapters?
    ): List<UserSelectedTopics>? {
        var list = arrayListOf<UserSelectedTopics>()
        var id = selectedChapter?.CHAPTER_ID
        if (id == chapter_id.toInt()) {
            var listofTopic = selectedChapter?.MAS_TOPICS
            if (listofTopic != null) {
                list.addAll(listofTopic)
            }
        }
        return list

    }

    private fun onPayClick() {
        getInitiatePayment()
    }

    private fun getInitiatePayment() {
        progressDialog.show()
        var userid = PrefrenceData.getUserId(this@TopicListActivity)
        var model = InitiatePaymentRequest(

                subject_id.toInt(),
                "TXN_PENDING",
                MainActivity.course_price,
                userid.toInt(),
                MainActivity.course_id
        )
        viewModel.getInitiatePayment(model)?.observe(this@TopicListActivity, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
                val body = it.body
                var list = body?.data
                onStartTransaction(list)
            } else {
                showFailedPaymentDialog()
            }
        })

    }

    private fun onStartTransaction(rsp: Data?) {
        var userid = PrefrenceData.getUserId(this@TopicListActivity)
        val Service =
                PaytmPGService.getStagingService("https://securegw-stage.paytm.in/order/process")
        val paramMap = HashMap<String, String>()
        var mid = "oBKImO82025244498334"
        paramMap.put("MID", mid)
        paramMap.put("CUST_ID", "CUST_" + userid)
        paramMap.put("ORDER_ID", "" + rsp?.ORDER_ID)
        paramMap.put("INDUSTRY_TYPE_ID", "Retail")
        paramMap.put("CHANNEL_ID", "WAP")
        paramMap.put("TXN_AMOUNT", MainActivity.course_price.toString())
        paramMap.put("WEBSITE", "APPSTAGING")
        paramMap.put(
                "CALLBACK_URL",
                "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + rsp?.ORDER_ID
        )
        paramMap.put("CHECKSUMHASH", rsp!!.CHECKSUM_VAL);
        val Order = PaytmOrder(paramMap)
        Service.initialize(Order, null)
        Service.startPaymentTransaction(this@TopicListActivity, true, true,
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
                                    ?.observe(this@TopicListActivity, Observer {
                                        progressDialog.dismiss()
                                        if (it.isSuccessful) {
                                            val body = it.body
                                            var list = body?.data
                                            dialogBuilder.dismiss()
                                            Toast.makeText(
                                                    this@TopicListActivity,
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
        val inflater = this@TopicListActivity.layoutInflater
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
        val inflater = this@TopicListActivity.layoutInflater
        val dialogViewSuccess: View = inflater.inflate(R.layout.payment_success_dialog, null)
        headerText = dialogViewSuccess.findViewById<View>(R.id.header_text) as TextView
        thanksString = dialogViewSuccess.findViewById<View>(R.id.thanks_string) as TextView
        txnSuccess = dialogViewSuccess.findViewById<View>(R.id.txn_success) as TextView
        closeBtn = dialogViewSuccess.findViewById<View>(R.id.close_btn) as MaterialButton
        paymentTickAnimation =
                dialogViewSuccess.findViewById<View>(R.id.animationView) as LottieAnimationView
        closeBtn.setOnClickListener {

            dialogBuilder.dismiss()
            val intent = Intent(this@TopicListActivity, DashBoardActivity::class.java)
            this@TopicListActivity.startActivity(intent)

        }

        dialogBuilderSuccess.setView(dialogViewSuccess)
        dialogBuilderSuccess.show()
    }

    private fun showFailedPaymentDialog() {
        val inflater = this@TopicListActivity.layoutInflater
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

    override fun onCellClickListener(
            selectedTopics: UserSelectedTopics?,
            topicPrevious: UserSelectedTopics?
    ) {
        if (selectedTopics != null && topicPrevious != null) {
            if (topicPrevious.MAS_TOPIC_STATUS != null && (topicPrevious.MAS_TOPIC_STATUS.VIDEO_STATUS.contentEquals("c"))) {
                selectedTopics.isEnable = true
            }
        }
        if (selectedTopics != null) {
            if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("f")) {
                if (selectedTopics.isEnable) {
                    if (selectedTopics.MAS_TOPIC_STATUS == null) {
                        val intent = Intent(this@TopicListActivity, NewPlayerActivity::class.java)
                        intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
                        intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
                        intent.putExtra(PlayerActivity.TOPIC_ID, "" + selectedTopics.TOPIC_ID)
                        intent.putExtra(PlayerActivity.USER_SUBS, "" + selectedTopics.TOPIC_SUBSCRIPTION)
                        intent.putExtra("IS_FROM_TOPIC", "Y")
                        intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + 0)
                        intent.putExtra(PlayerActivity.TEST_STATUS, "")
                        intent.putExtra(PlayerActivity.VIDEO_SEEN_STATUS, "")
                        this@TopicListActivity.startActivity(intent)
                    }
                    if (selectedTopics?.MAS_TOPIC_STATUS != null) {
                        val intent = Intent(this@TopicListActivity, NewPlayerActivity::class.java)
                        intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
                        intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
                        intent.putExtra(PlayerActivity.TOPIC_ID, "" + selectedTopics.TOPIC_ID)
                        intent.putExtra(PlayerActivity.USER_SUBS, "" + selectedTopics.TOPIC_SUBSCRIPTION)
                        intent.putExtra("IS_FROM_TOPIC", "Y")
                            intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + selectedTopics.MAS_TOPIC_STATUS.TOPIC_STATUS_ID)
                            intent.putExtra(PlayerActivity.TEST_STATUS, selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS)
                            intent.putExtra(PlayerActivity.VIDEO_SEEN_STATUS, "" + selectedTopics.MAS_TOPIC_STATUS.VIDEO_STATUS)
                        this@TopicListActivity.startActivity(intent)
                    }
                }else {
                    Toast.makeText(this@TopicListActivity, "Please complete above topics/Videos before this topic.", Toast.LENGTH_LONG).show()
                }

            } else if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("p")) {
                if (MainActivity.subscription_flag.contentEquals("p")) {
                    if (selectedTopics?.isEnable) {
                        if (selectedTopics?.MAS_TOPIC_STATUS == null) {
                            val intent = Intent(this@TopicListActivity, NewPlayerActivity::class.java)
                            intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
                            intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
                            intent.putExtra(PlayerActivity.TOPIC_ID, "" + selectedTopics.TOPIC_ID)
                            intent.putExtra(PlayerActivity.USER_SUBS, "" + selectedTopics.TOPIC_SUBSCRIPTION)
                            intent.putExtra("IS_FROM_TOPIC", "Y")
                            intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + 0)
                            intent.putExtra(PlayerActivity.TEST_STATUS, "")
                            intent.putExtra(PlayerActivity.VIDEO_SEEN_STATUS, "")
                            this@TopicListActivity.startActivity(intent)
                        }
                        if (selectedTopics.MAS_TOPIC_STATUS != null) {
                            val intent = Intent(this@TopicListActivity, NewPlayerActivity::class.java)
                            intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
                            intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
                            intent.putExtra(PlayerActivity.TOPIC_ID, "" + selectedTopics.TOPIC_ID)
                            intent.putExtra(PlayerActivity.USER_SUBS, "" + selectedTopics.TOPIC_SUBSCRIPTION)
                            intent.putExtra("IS_FROM_TOPIC", "Y")
                            intent.putExtra(PlayerActivity.TOPIC_STATUS_ID, "" + selectedTopics.MAS_TOPIC_STATUS.TOPIC_STATUS_ID)
                            intent.putExtra(PlayerActivity.TEST_STATUS, selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS)
                            intent.putExtra(PlayerActivity.VIDEO_SEEN_STATUS, "" + selectedTopics.MAS_TOPIC_STATUS.VIDEO_STATUS)
                            this@TopicListActivity.startActivity(intent)
                        }
                    }else {
                        Toast.makeText(this@TopicListActivity, "Please complete above pending topics/Videos before this topic.", Toast.LENGTH_LONG).show()
                    }
                } else if (MainActivity.subscription_flag.contentEquals("u")) {
                    if (MainActivity.course_price.toInt() > 0) {
                        showDialog()
                    } else {
                        Toast.makeText(
                                this@TopicListActivity,
                                "Price for payment must be greater than 0.",
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCellClickListenerPDF(selectedTopics: UserSelectedTopics?) {
        if (selectedTopics != null) {
            if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("f")) {
                if (selectedTopics.BOOK_URL != null) {
                    ViewDocumentHelper.navigateToCustomTab(
                            this@TopicListActivity,
                            selectedTopics.BOOK_URL
                    )
                }
            } else if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("p")) {
                if (MainActivity.subscription_flag.contentEquals("p")) {
                    if (selectedTopics.BOOK_URL != null) {
                        ViewDocumentHelper.navigateToCustomTab(
                                this@TopicListActivity,
                                selectedTopics.BOOK_URL
                        )
                    }
                } else if (MainActivity.subscription_flag.contentEquals("u")) {
                    if (MainActivity.course_price.toInt() > 0) {
                        showDialog()
                    } else {
                        Toast.makeText(
                                this@TopicListActivity,
                                "Price for payment must be greater than 0.",
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCellClickListenerCard(selectedTopics: UserSelectedTopics?) {
        if (selectedTopics != null) {
            MainActivity.topic_id = selectedTopics.TOPIC_ID.toString()
            if (selectedTopics.TOPIC_ID != null) {
                if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("f")) {
                    if (selectedTopics.MAS_TOPIC_STATUS != null) {
                        if ((selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("") || selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("p")
                                        || selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("f")) && (selectedTopics.MAS_TOPIC_STATUS.VIDEO_STATUS.contentEquals("c"))) {
                            val intent = Intent(this@TopicListActivity, InitializQuizActivity::class.java)
                            intent.putExtra(QuizActivityNew.KEY_SELECTED_TOPIC, selectedTopics)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@TopicListActivity, "Please complete video before test.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@TopicListActivity, "Please complete video before test.", Toast.LENGTH_LONG
                        ).show()
                    }
                } else if (selectedTopics.TOPIC_SUBSCRIPTION.contentEquals("p")) {

                    if (MainActivity.subscription_flag.contentEquals("p")) {
                        if (selectedTopics.MAS_TOPIC_STATUS != null) {
                            if ((selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("") || selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("p")
                                            || selectedTopics.MAS_TOPIC_STATUS.TEST_STATUS.contentEquals("f")) && (selectedTopics.MAS_TOPIC_STATUS.VIDEO_STATUS.contentEquals("c"))) {
                                val intent = Intent(this@TopicListActivity, InitializQuizActivity::class.java)
                                intent.putExtra(QuizActivityNew.KEY_SELECTED_TOPIC, selectedTopics)
                                startActivity(intent)
                            }else {
                                Toast.makeText(this@TopicListActivity, "Please complete video before test.", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this@TopicListActivity, "Please complete video before test.", Toast.LENGTH_LONG).show()
                        }
                    } else if (MainActivity.subscription_flag.contentEquals("u")) {

                        if (MainActivity.course_price.toInt() > 0) {
                            showDialog()
                        } else {
                            Toast.makeText(this@TopicListActivity, "Price for payment must be greater than 0.", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            } else {
                Toast.makeText(this@TopicListActivity, "Please complete video before test.", Toast.LENGTH_LONG).show()
            }
        }
    }
}


