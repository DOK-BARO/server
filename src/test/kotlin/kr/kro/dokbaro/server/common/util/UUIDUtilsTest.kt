package kr.kro.dokbaro.server.common.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.UUID

class UUIDUtilsTest :
	StringSpec({

		"byteArray 에서 UUID로 변환한다" {
			val target = UUID.randomUUID()

			val byteArray = UUIDUtils.uuidToByteArray(target)
			val result = UUIDUtils.byteArrayToUUID(byteArray)

			target shouldBe result
		}

		"String에서 UUID로 변환한다." {
			val target = UUID.randomUUID()

			val byteArray = UUIDUtils.uuidToString(target)
			val result = UUIDUtils.stringToUUID(byteArray)

			target shouldBe result
		}
	})