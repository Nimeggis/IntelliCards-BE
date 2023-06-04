package api.graphql.config

import com.expediagroup.graphql.generator.extensions.deepName
import com.expediagroup.graphql.generator.extensions.unwrapType
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.scalars.datetime.DateTimeScalar
import graphql.schema.*
import java.net.URI
import java.time.Duration
import java.time.OffsetDateTime
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

/**
 * SchemaGeneratorHooks which support the [TypeGraphQLType] annotation
 * Also adds support for scalars:
 * - [OffsetDateTime] -> DateTime
 * - [URI] -> Url
 * - Duration -> Duration
 *
 * Handles the automatic generation of payload types for mutations annotated with [AutoPayloadType]
 */
object DefaultSchemaGeneratorHooks : SchemaGeneratorHooks {

    /**
     * Code registry used for additional datafetchers
     */
    val codeRegistry = GraphQLCodeRegistry.newCodeRegistry()

    override fun didGenerateMutationField(
        kClass: KClass<*>, function: KFunction<*>, fieldDefinition: GraphQLFieldDefinition
    ): GraphQLFieldDefinition {
        return if (function.hasAnnotation<AutoPayloadType>()) {
            val fieldName = fieldDefinition.type.asFieldName
            val description = function.findAnnotation<AutoPayloadType>()!!.description
            val payloadType =
                GraphQLObjectType.newObject().name(fieldDefinition.name.replaceFirstChar(Char::titlecase) + "Payload")
                    .field {
                        it.name(fieldName).description(description).type(fieldDefinition.type.nullable)
                    }.build()
            codeRegistry.dataFetcher(
                FieldCoordinates.coordinates(payloadType, fieldName),
                DataFetcher<Any> { it.getSource() })
            fieldDefinition.transform { it.type(payloadType) }
        } else {
            super.didGenerateMutationField(kClass, function, fieldDefinition)
        }
    }

    /**
     * Transforms the name of the type to a field name
     * Maps all starting titlecase letters to lowercase
     */
    private val GraphQLOutputType.asFieldName: String
        get() {
            val unwrappedName = unwrapType().deepName
            val pattern = "^(([A-Z](?=[a-z]))|([A-Z]+(?=[A-Z][a-z]))|([A-Z]+$))".toRegex()
            return pattern.replace(unwrappedName) {
                it.value.map(Char::lowercase).joinToString("")
            }
        }

    /**
     * Gets a nullable version of the type
     */
    private val GraphQLOutputType.nullable: GraphQLOutputType
        get() {
            return if (this is GraphQLNonNull) {
                this.originalWrappedType as GraphQLOutputType
            } else {
                this
            }
        }

    override fun willBuildSchema(builder: GraphQLSchema.Builder): GraphQLSchema.Builder {
        val oldCodeRegistry = builder.build().codeRegistry
        val newCodeRegistry = oldCodeRegistry.transform {
            it.dataFetchers(codeRegistry.build())
        }
        return builder.codeRegistry(newCodeRegistry)
    }

}