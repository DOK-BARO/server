package kr.kro.dokbaro.server.core.book.application.service.auth

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import kr.kro.dokbaro.server.common.exception.http.status4xx.default.DefaultForbiddenException
import kr.kro.dokbaro.server.fixture.domain.dokbaroAdminFixture
import kr.kro.dokbaro.server.fixture.domain.dokbaroUserFixture

class BookAuthorityCheckServiceTest :
	StringSpec({
		val bookAuthorityCheckService = BookAuthorityCheckService()
		"카테고리 생성 시 admin이 아니면 예외를 반환한다" {
			shouldThrow<DefaultForbiddenException> {
				bookAuthorityCheckService.checkCreateBookCategoryAuthority(dokbaroUserFixture())
			}

			shouldNotThrow<DefaultForbiddenException> {
				bookAuthorityCheckService.checkCreateBookCategoryAuthority(dokbaroAdminFixture())
			}
		}

		"책 생성 시 admin이 아니면 예외를 반환한다" {
			shouldThrow<DefaultForbiddenException> {
				bookAuthorityCheckService.checkCreateBookAuthority(dokbaroUserFixture())
			}

			shouldNotThrow<DefaultForbiddenException> {
				bookAuthorityCheckService.checkCreateBookAuthority(dokbaroAdminFixture())
			}
		}
	})