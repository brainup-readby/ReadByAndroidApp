package com.brainup.readbyapp.rest

import androidx.lifecycle.LiveData
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.auth.registration.BoardData
import com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic.SubscriptionModel
import com.brainup.readbyapp.com.brainup.readbyapp.auth.registration.UserInfo
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.model.model.VideoRequestModel
import com.brainup.readbyapp.payment.model.InitiatePaymentRequest
import com.brainup.readbyapp.payment.model.InitiatePaymentResponse
import com.brainup.readbyapp.quiz.model.model.*
import com.brainup.readbyapp.utils.Constants
import retrofit2.http.*


interface ApiInterface {
    @GET(Routs.LOGIN)
    fun postLogin(
        @Query(Constants.QUERY_MOB_NO) mobNo: String?
    ): LiveData<ApiResponse<CommonResponse<String>>>

    @GET(Routs.SEND_OTP)
    fun getOpt(@Query(Constants.QUERY_MOB_NO) mobNo: String?): LiveData<ApiResponse<CommonResponse<String>>>

    @GET(Routs.OTP_VERIFICATION)
    fun otpVerification(
        @Query(Constants.QUERY_MOB_NO) mobNo: String,
        @Query(Constants.QUERY_OTP) otp: String
    ): LiveData<ApiResponse<CommonResponse<Boolean>>>

    @GET(Routs.OTP_LOGIN_VERIFICATION)
    fun otpLoginVerification(
        @Query(Constants.QUERY_MOB_NO) mobNo: String,
        @Query(Constants.QUERY_OTP) otp: String
    ): LiveData<ApiResponse<CommonResponse<UserData>>>

    @POST(Routs.REGISTRATION)
    fun postRegistration(@Body userData: UserInfo): LiveData<ApiResponse<CommonResponse<UserData>>>

    @GET(Routs.BOARD_DETAILS)
    fun getBoardDetails(): LiveData<ApiResponse<CommonResponse<List<BoardData>>>>

    @POST(Routs.SUBSCRIBE_COURSE)
    fun subscribeCourse(@Body subs: SubscriptionModel): LiveData<ApiResponse<CommonResponse<SubscriptionModel>>>

    @GET(Routs.RESEND_OTP)
    fun resendOtp(@Query(Constants.QUERY_MOB_NO) mobNo: String?): LiveData<ApiResponse<CommonResponse<String>>>

    @GET(Routs.USER_INFO)
    fun getUserInfo(@Query("userId") userId: String): LiveData<ApiResponse<CommonResponse<UserData>>>

    @GET(Routs.GET_QUIZ_LIST)
    fun getQuizList(@Query("topicId") id: String): LiveData<ApiResponse<QusListResponse>>



    @POST(Routs.SUBMIT_QUIZ_LIST)
    fun submitQuizList(@Body list : QusRequestModel): LiveData<ApiResponse<SubmitQusResponse>>



    @POST(Routs.UPDATE_TOPIC_STATUS)
    fun updateTopicFlag(@Body list : TestRequestModel): LiveData<ApiResponse<TopicStatusResposeModel>>


    @POST(Routs.UPDATE_TOPIC_STATUS)
    fun updateVideoFlag(@Body list : VideoRequestModel): LiveData<ApiResponse<TopicStatusResposeModel>>



    @POST(Routs.GET_INITIAL_PAYMENTS)
    fun getInitiatePayment(@Body list : InitiatePaymentRequest): LiveData<ApiResponse<InitiatePaymentResponse>>



    /*@GET(Routs.POST_LOGIN)
    fun postLogin(
        @Query("username") userName: String?,
        @Query("password") passWord: String?
    ): LiveData<ApiResponse<ResponseBody>>

    @POST(Routs.POST_DEAL)
    fun postDeal(
        @Query("username") userName: String?,
        @Query("password") passWord: String?
    ): LiveData<ApiResponse<ResponseBody>>

    @DELETE(Routs.DELETE_DEAL)
    fun deleteDeal(
        @Query("username") userName: String?,
        @Query("password") passWord: String?
    ): LiveData<ApiResponse<ResponseBody>>

    @PUT(Routs.DELETE_DEAL)
    fun updateDeal(
        @Query("username") userName: String?,
        @Query("password") passWord: String?
    ): LiveData<ApiResponse<ResponseBody>>

    @POST(Routs.POST_DEAL)
    fun publishDeal(passWord: String?): LiveData<ApiResponse<ResponseBody>>

    @GET(Routs.POST_DEAL)
    fun getAllDeals(passWord: String?): LiveData<ApiResponse<ResponseBody>>

    @POST(Routs.SEND_OTP)
    fun getOpt(@Query("mobileNo") mobNo: String?): LiveData<ApiResponse<OtpResponse>>

    @GET(Routs.OTP_VERIFICATION)
    fun otpVerification(
        @Path("mobNo") mobNo: String,
        @Path("otp") otp: String
    ): LiveData<ApiResponse<OtpResponse>>

    @GET(Routs.GET_BOARDS)
    fun getAllBoards(): LiveData<ApiResponse<ArrayList<String>>>

    @GET(Routs.GET_CLASSES)
    fun getAllClasses(): LiveData<ApiResponse<ArrayList<String>>>

    @GET(Routs.GET_SESSIONS)
    fun getAllSessions(): LiveData<ApiResponse<ArrayList<String>>>

    @GET(Routs.GET_MEDIUMS)
    fun getAllMediums(): LiveData<ApiResponse<ArrayList<String>>>

    @POST(Routs.REGISTRATION)
    fun doRegistration(@Body user: RegistrationModel): LiveData<ApiResponse<CommonResponse>>

    @POST(Routs.LOGIN)
    fun doLogin(
        @Path("mobNo") mobNo: String,
        @Path("pwd") pwd: String
    ): LiveData<ApiResponse<LoginResponse>>

    @GET(Routs.GET_FREE_VIDEOS)
    fun getFreeVideos(@Path("id") id: String): LiveData<ApiResponse<List<FreeVideoResponse>>>

    @GET(Routs.GET_QUESTION_BANK)
    fun getQuestionBankList(@Path("id") id: String): LiveData<ApiResponse<ArrayList<QuestionPaper>>>

    @Multipart
    @PUT(Routs.PUT_PROFILE_IMAGE)
    fun updateProfileImage(
        @Path("id") id: String,
        @Part file: MultipartBody.Part,
        @Part("id") idBody: RequestBody
    ): LiveData<ApiResponse<ResponseBody>>

    @PUT(Routs.PUT_RESET_PASSWORD)
    fun resetPassword(
        @Path("id") id: String,
        @Query("oldPassword") passWord: String?,
        @Query("newPassword") newPassword: String?
    ): LiveData<ApiResponse<Boolean>>

    @PUT(Routs.UPDATE_PROFILE)
    fun updateProfile(
        @Query("id") id: String,
        @Query("mobileNo") mobileNo: String,
        @Query("email") email: String,
        @Query(
            "name"
        ) name: String
    ): LiveData<ApiResponse<CommonResponse>>

    @GET(Routs.GET_SUBSCRIPTION_LIST)
    fun getSubscriptionDetails(@Query("id") id: String): LiveData<ApiResponse<List<SubjectModel>>>

    @PUT(Routs.SUBSCRIBED_SUBJECT)
    fun subscribedSubject(
        @Query("mobileUserId") id: String,
        @Query("suscriptionId") suscriptionId: List<Int>
    ): LiveData<ApiResponse<Boolean>>

    @GET(Routs.GET_SUBSCRIBED_VIDEOS)
    fun getVideosSubjectWise(
        @Path("id") id: String,
        @Path("subjectName") subjectName: String
    ): LiveData<ApiResponse<List<FreeVideoResponse>>>

    @PUT(Routs.FORGOT_PASSWORD)
    fun forgotPassword(@Path("mobNo") mobNo: String): LiveData<ApiResponse<String>>

    @PUT(Routs.CREATE_PWD)
    fun createPwd(
        @Path("mobNo") mobNo: String,
        @Path("otp") otp: String,
        @Path("pwd") pwd: String
    ): LiveData<ApiResponse<Boolean>>

    @PUT(Routs.SUBSCRIBED_QUESTION_BANK)
    fun subscribedQuestionPaper(
        @Query("mobileUserId") id: String,
        @Query("questionbankId") questionbankId: String
    ): LiveData<ApiResponse<Boolean>>

    @GET(Routs.GET_TEST_QUIZ)
    fun getQuizList(@Path("id") id: String): LiveData<ApiResponse<ArrayList<TestQuiz>>>

    @PUT(Routs.PARTICIPATE)
    fun participated(
        @Path("mobileUserId") id: String,
        @Path("quizId") questionbankId: String
    ): LiveData<ApiResponse<ParticipateResponse>>

    @GET(Routs.START_QUIZ)
    fun getQuestionList(
        @Path("mobileUserId") userId: String,
        @Path("quizId") id: String
    ): LiveData<ApiResponse<QuizResponse>>

    @POST(Routs.POST_QUIZ)
    fun submitQuiz(
        @Body body: SubmitQuestionRequestModel
    ): LiveData<ApiResponse<SubmitQuizResponse>>

    @POST(Routs.POST_REG)
    fun postReg(@Body studentModel: StudentModel): LiveData<ApiResponse<RegResponse>>
*/
}
