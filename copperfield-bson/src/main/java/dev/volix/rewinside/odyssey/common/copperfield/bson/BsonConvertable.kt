package dev.volix.rewinside.odyssey.common.copperfield.bson

import dev.volix.rewinside.odyssey.common.copperfield.Convertable

/**
 * This interface indicates, that this class may be converted to and from [org.bson.Document]
 * instances using a [BsonRegistry].
 *
 * @author Benedikt Wüller
 */
interface BsonConvertable : Convertable
