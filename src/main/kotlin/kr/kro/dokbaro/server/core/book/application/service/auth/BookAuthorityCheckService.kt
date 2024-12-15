package kr.kro.dokbaro.server.core.book.application.service.auth

import kr.kro.dokbaro.server.common.annotation.AuthorityCheckService
import kr.kro.dokbaro.server.common.exception.http.status4xx.default.DefaultForbiddenException
import kr.kro.dokbaro.server.core.member.domain.Role
import kr.kro.dokbaro.server.security.details.DokbaroUser

@AuthorityCheckService
class BookAuthorityCheckService {
	fun checkCreateBookCategoryAuthority(user: DokbaroUser) {
		if (!user.hasRole(Role.ADMIN)) {
			throw DefaultForbiddenException()
		}
	}
}