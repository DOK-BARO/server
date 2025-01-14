package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.SocialAccount

/**
 * social 계정을 insert하는 port 입니다.
 */
fun interface InsertSocialAccountPort {
	fun insertSocialAccount(socialAccount: SocialAccount)
}