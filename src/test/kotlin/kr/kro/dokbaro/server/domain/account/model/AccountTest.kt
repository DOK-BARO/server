package kr.kro.dokbaro.server.domain.account.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.Clock

class AccountTest :
	StringSpec({

		"초기 설정을 수행한다" {
			val socialId = "dasf"
			val clock = Clock.systemUTC()
			val provider = Provider.KAKAO

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