package kr.kro.dokbaro.server.etc

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class BuildGradleKtsTest :
	StringSpec({

		"datasource url에서 schema를 추출한다" {
			// 맨 마지막 슬래시와 파라미터(?) 사이의 schema 값만 추출
			val url = "jdbc:mysql://mysql/bewineddev?serverTimezone=Asia/Seoul"
			val regex = Regex("""jdbc:mysql://[^/]+/([^?]+)""")

			val matchResult = regex.find(url)
			val databaseName = matchResult?.groups?.get(1)?.value

			databaseName shouldBe "bewineddev"
		}
	})