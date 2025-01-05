package kr.kro.dokbaro.server.common.dto.response

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PageResponseTest :
	StringSpec({

		"page 끝 번호를 반환한다" {
			PageResponse
				.of(
					10,
					10,
					emptyList<String>(),
				).endPageNumber shouldBe 1

			PageResponse
				.of(
					20,
					10,
					emptyList<String>(),
				).endPageNumber shouldBe 2

			PageResponse
				.of(
					15,
					10,
					emptyList<String>(),
				).endPageNumber shouldBe 2

			PageResponse
				.of(
					5,
					10,
					emptyList<String>(),
				).endPageNumber shouldBe 1
		}
	})