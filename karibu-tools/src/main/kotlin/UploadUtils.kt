package com.github.mvysny.kaributools

import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.UploadI18N
import elemental.json.Json

/**
 * Enables or disables the upload, by setting [Upload.setMaxFiles] to either 0 or 1.
 *
 * Vote for https://github.com/vaadin/flow-components/issues/2182
 */
public var Upload.isEnabled: Boolean
    get() = maxFiles > 0
    set(value) {
        maxFiles = if (value) 1 else 0
    }

/**
 * Clears the list of uploaded files.
 *
 * Vote for https://github.com/vaadin/flow-components/issues/1572 .
 */
public fun Upload.clear() {
    if (VaadinVersion.get.isAtLeast(25)) {
        val m = element.javaClass.getMethod("setPropertyList", String::class.java, List::class.java)
        m.invoke(element, "files", listOf<Any>())
    } else {
        element.setPropertyJson("files", Json.createArray())
    }
}

/**
 * Gets/sets upload button caption.
 */
public var Upload.buttonCaption: String?
    get() = i18n?.addFiles?.one
    set(value) {
        if (i18n == null) {
            i18n = UploadI18N()
        }
        if (i18n.addFiles == null) {
            i18n.addFiles = UploadI18N.AddFiles()
        }
        i18n.addFiles.one = value
        i18n.addFiles.many = value
    }
