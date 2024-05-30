package com.dede.dedegame.presentation.common.tracker

object TrackerFactory {

    private var trackerMap = hashMapOf<String, ITracker<*>>()

    fun <T : ITrackerModel> getTracker(type: Class<T>): ITracker<T>? {
        val className = type.name
        var tracker = trackerMap[className]
        if (tracker != null) {
            return tracker as ITracker<T>
        } else {
            if (DedeFirebaseTrackerModel::class.java.isAssignableFrom(type)) {
                tracker = DedeFirebaseTracker
            }
            if (tracker != null) {
                trackerMap[className] = tracker
            }

            return tracker as ITracker<T>?
        }
    }
}