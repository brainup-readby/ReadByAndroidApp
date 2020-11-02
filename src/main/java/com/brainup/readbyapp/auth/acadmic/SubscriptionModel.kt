package com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic

class SubscriptionModel(
    var STREAM_ID: Long? = 1,
    var YEAR_ID: Long? = 1,
    var IS_ACTIVE: String = "t",
    var CREATED_BY: String = "",
    var UPDATED_BY: String = "",
    var DEVICE_ID: String = "android",
    var IS_EXPIRED: String = "y",
    var MOBILE_NO: String,
    var COURSE_ID: Long? = 1,
    var BOARD_ID: Long? = 1,
    var INSTITUTION_NAME: String = "DAV college",
    var USER_ID: String = ""
)