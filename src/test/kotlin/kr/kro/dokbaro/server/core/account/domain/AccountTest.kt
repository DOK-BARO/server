package kr.kro.dokbaro.server.core.account.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.global.AuthProvider
import java.time.Clock

class AccountTest :
	StringSpec({

		"초기 설정을 수행한다" {
			val socialId = "dasf"
			val clock = Clock.systemUTC()
			val provider = AuthProvider.KAKAO

			val result =
				Account.init(
					socialId,
					provider,
					clock,
				)

			result.id shouldNotBe null
			result.registeredAt shouldNotBe null
			result.socialId shouldBe socialId
			result.provider shouldBe provider
		}
	})