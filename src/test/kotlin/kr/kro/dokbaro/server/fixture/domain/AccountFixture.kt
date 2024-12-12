package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.account.domain.AccountPassword

fun accountPasswordFixture(
	id: Long = Constants.UNSAVED_ID,
	password: String = "password",
	memberId: Long = 1,
) = AccountPassword(
	id,
	password,
	memberId,
)