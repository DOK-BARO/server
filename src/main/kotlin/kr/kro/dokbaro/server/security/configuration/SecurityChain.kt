package kr.kro.dokbaro.server.security.configuration

import org.springframework.security.config.annotation.web.builders.HttpSecurity

fun interface SecurityChain {
	fun link(origin: HttpSecurity): HttpSecurity
}