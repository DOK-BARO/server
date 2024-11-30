package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.core.member.application.port.input.dto.CertificatedMember
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import java.util.UUID

fun memberFixture(
	id: Long = 0,
	nickName: String = "testNick",
	email: Email = Email("example@gmail.com"),
	profileImage: String? = "hello.png",
	certificationId: UUID = UUID.randomUUID(),
	roles: Set<Role> = setOf(Role.GUEST),
): Member =
	Member(
		id,
		nickName,
		email,
		profileImage,
		certificationId,
		roles,
	)

fun certificatedMemberFixture(
	nickName: String = "testNick",
	email: String = "wwww@gmail.com",
	profileImage: String? = null,
	certificationId: UUID = UUID.randomUUID(),
	roles: Set<Role> = setOf(Role.GUEST),
	id: Long = 0,
) = CertificatedMember(
	nickName = nickName,
	email = email,
	profileImage = profileImage,
	certificationId = certificationId,
	roles = roles,
	id = id,
)