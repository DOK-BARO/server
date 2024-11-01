package kr.kro.dokbaro.server.core.notification.domain

interface ContentStrategy {
	fun getContent(): String
}