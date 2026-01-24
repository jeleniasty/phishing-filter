package com.jeleniasty.phishingfilter.modules.processing.model

enum class SubscriptionActionType{
 START, STOP;

 companion object {
  fun fromMessage(message: String): SubscriptionActionType? =
   entries.firstOrNull { it.name.equals(message.trim(), ignoreCase = true) }
 }
}