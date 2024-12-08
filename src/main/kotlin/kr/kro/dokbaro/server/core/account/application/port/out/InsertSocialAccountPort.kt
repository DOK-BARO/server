package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.SocialAccount

fun interface InsertSocialAccountPort {
	fun insertSocialAccount(socialAccount: SocialAccount)
}