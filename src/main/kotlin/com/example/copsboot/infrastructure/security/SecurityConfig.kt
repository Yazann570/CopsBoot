package com.example.copsboot.infrastructure.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "copsboot-security")
class SecurityConfiguration {
    var mobileAppClientId: String = ""
    var mobileAppClientSecret: String = ""
}