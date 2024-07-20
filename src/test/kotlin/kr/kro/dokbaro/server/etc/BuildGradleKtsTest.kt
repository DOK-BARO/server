package kr.kro.dokbaro.server.etc

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class BuildGradleKtsTest :
	StringSpec({

		"datasource url에서 schema를 추출한다" {
			val url = "jdbc:mysql://mysql/bewineddev?serverTimezone=Asia/Seoul"
			val regex = Regex("""jdbc:mysql://[^/]+/([^?]+)""")

			val result =
				url?.let {
					regex
						.find(it)
						?.groups
						?.get(1)
						?.value
				} ?: "mydatabase"

			result shouldBe "bewineddev"
		}
	})