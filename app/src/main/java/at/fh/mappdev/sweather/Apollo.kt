package at.fh.mappdev.sweather

import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://api.mappdev.jslabs.at/api/v1/graphql")
    .build()