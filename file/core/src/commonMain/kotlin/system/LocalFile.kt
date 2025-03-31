package system

/**
 * An abstract representation of a file found on disk
 *
 * On the Browser (JS & Wasm): A local file is an instance that wraps a File object
 * Everywhere else: A [LocalFile] is just a Path to a file on disk
 */
interface LocalFile