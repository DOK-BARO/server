package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.Account

fun interface LoadAccountPort {
	fun findBy(socialId: String): Account?
}