package kr.kro.dokbaro.server.domain.account.port.output

import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.global.template.port.LoadPort

interface LoadAccountPort : LoadPort<String, Account>