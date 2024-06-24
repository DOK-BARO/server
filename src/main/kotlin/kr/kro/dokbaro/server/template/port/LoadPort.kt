package kr.kro.dokbaro.server.template.port

interface LoadPort<K, V> {
	fun findBy(k: K): V?
}