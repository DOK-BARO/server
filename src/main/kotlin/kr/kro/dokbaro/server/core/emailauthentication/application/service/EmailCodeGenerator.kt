package kr.kro.dokbaro.server.core.emailauthentication.application.service

fun interface EmailCodeGenerator {
	fun generate(): String
}