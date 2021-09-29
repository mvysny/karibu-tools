package com.github.mvysny.kaributools

import com.vaadin.flow.component.upload.Upload

/**
 * Enables or disables the upload, by setting [Upload.setMaxFiles] to either 0 or 1.
 *
 * Vote for https://github.com/vaadin/flow-components/issues?q=is%3Aissue+upload+hasenabled+
 */
public var Upload.isEnabled: Boolean
    get() = maxFiles > 0
    set(value) {
        maxFiles = if (value) 1 else 0
    }
