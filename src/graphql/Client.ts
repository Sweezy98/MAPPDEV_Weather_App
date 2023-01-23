import { ApolloClient, InMemoryCache, HttpLink, from } from '@apollo/client';

import { onError } from '@apollo/client/link/error';

//import authMiddleware from './middleware/auth';


const errorLink = onError(({ graphQLErrors, networkError }) => {
    if (graphQLErrors) {
        graphQLErrors.map(({ message, locations, path }) => {
            console.log(`Graphql error: ${message}`);
        });
    }
    if (networkError) {
        networkError.message = "network error";
        console.log(networkError.message);
    }
});

const link = from([
    //authMiddleware,
    errorLink,
    new HttpLink({uri: "https://api.mappdev.jslabs.at/api/v1/graphql"}),
]);

const client = new ApolloClient({
    connectToDevTools: true,
    cache: new InMemoryCache({resultCaching: false}),
    link: link
});

export default client