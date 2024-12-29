package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.core.member.domain.AccountType
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import java.util.UUID

fun memberFixture(
	id: Long = 0,
	nickName: String = "testNick",
	email: Email? = Email("example@gmail.com"),
	profileImage: String? = "hello.png",
	certificationId: UUID = UUID.randomUUID(),
	roles: Set<Role> = setOf(Role.GUEST),
	accountType: AccountType = AccountType.SOCIAL,
): Member =
	Member(
		id,
		nickName,
		email,
		profileImage,
		certificationId,
		roles,
		accountType = accountType,
	)