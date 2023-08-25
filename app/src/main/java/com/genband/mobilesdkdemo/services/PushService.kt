package com.genband.mobilesdkdemo.services

import com.genband.mobile.api.services.push.PushServiceInterface
import com.genband.mobile.api.services.push.PushSubscriptionCallback
import com.genband.mobile.api.services.push.PushSubscriptionListener
import com.genband.mobile.api.services.push.PushUnsubscriptionCallback
import com.genband.mobile.api.services.push.PushUpdateSubscriptionCallback

class PushService : PushServiceInterface  {
    override fun setPushSubscriptionListener(p0: PushSubscriptionListener?) {
        TODO("Not yet implemented")
    }

    override fun subscribeToPushNotifications(
        p0: String?,
        p1: String?,
        p2: String?,
        p3: PushSubscriptionCallback?
    ) {
        TODO("Not yet implemented")
    }

    override fun unsubscribeFromPushNotifications(p0: String?, callback: PushUnsubscriptionCallback?) {
        TODO("Not yet implemented")
    }

    override fun updatePushSubscription(
        p0: String?,
        p1: String?,
        p2: String?,
        p3: PushUpdateSubscriptionCallback?
    ) {
        TODO("Not yet implemented")
    }

    override fun injectPushMessage(p0: MutableMap<String, String>?) {
        TODO("Not yet implemented")
    }
}