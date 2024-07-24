package com.dede.dedegame.domain.model

import java.io.Serializable

data class VersionUpdate(val task: Map<String, Any>) : Serializable {

    var content: String? = null
        get() {
            return task["content"]?.toString() ?: field
        }
        set(value) {
            field = value
        }

    var canClose: Boolean = false
        get() {
            return task["can_close"]?.toString()?.toBoolean() ?: field
        }
        set(value) {
            field = value
        }

    var versionCode: Int? = null
        get() {
            return task["version_code"]?.toString()?.toInt() ?: field
        }
        set(value) {
            field = value
        }

    var nameAction: String? = null
        get() {
            return task["name_action"]?.toString() ?: field
        }
        set(value) {
            field = value
        }

    var linkAction: String? = null
        get() {
            return task["link_action_android"]?.toString() ?: field
        }
        set(value) {
            field = value
        }

    var title: String? = null
        get() {
            return task["title"]?.toString() ?: field
        }
        set(value) {
            field = value
        }
}
