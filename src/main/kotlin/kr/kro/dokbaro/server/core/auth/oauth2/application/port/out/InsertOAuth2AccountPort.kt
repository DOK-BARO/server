package kr.kro.dokbaro.server.core.auth.oauth2.application.port.out

import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2Account

fun interface InsertOAuth2AccountPort {
	fun insert(oAuth2Account: OAuth2Account): Long
}