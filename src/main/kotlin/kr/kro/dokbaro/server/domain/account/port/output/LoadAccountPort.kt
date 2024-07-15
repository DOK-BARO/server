package kr.kro.dokbaro.server.domain.account.port.output

import kr.kro.dokbaro.server.domain.account.model.Account

fun interface LoadAccountPort {
	fun findBy(socialId: String): Account?
}