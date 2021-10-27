package com.github.mvysny.kaributools

import com.vaadin.flow.component.upload.Upload
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
    element.setPropertyJson("files", Json.createArray())
}
