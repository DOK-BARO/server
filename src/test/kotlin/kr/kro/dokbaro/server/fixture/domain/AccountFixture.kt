package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.account.domain.EmailAccount
import kr.kro.dokbaro.server.security.details.DokbaroUser
import java.util.UUID

fun emailAccountFixture(
	id: Long = Constants.UNSAVED_ID,
	email: String = "example@hello.com",
	password: String = "password",
	memberId: Long = 1,
) = EmailAccount(
	id,
	email = email,
	password,
	memberId,
)

fun dokbaroUserFixture(
	id: Long = Constants.UNSAVED_ID,
	certificationId: UUID = UUID.randomUUID(),
	nickname: String = "hello",
	email: String = "hello@gmail.com",
	role: Collection<String> = listOf("GUEST"),
): DokbaroUser =
	DokbaroUser(
		id = id,
		certificationId = certificationId,
		nickname = nickname,
		email = email,
		role = role,
	)

fun dokbaroAdminFixture(
	id: Long = Constants.UNSAVED_ID,
	certificationId: UUID = UUID.randomUUID(),
	nickname: String = "hello",
	email: String = "hello@gmail.com",
	role: Collection<String> = listOf("GUEST", "ADMIN"),
): DokbaroUser =
	DokbaroUser(
		id = id,
		certificationId = certificationId,
		nickname = nickname,
		email = email,
		role = role,
	)