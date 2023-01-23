import { gql } from '@apollo/client';

export const sayHello = gql`
    query sayHello($name: String!) {
        sayHello(name: $name)
    }
`

export const getLogs = gql`
    query getLogs($limit: Int, $offset: Int) {
        getLogs(limit: $limit, offset: $offset) {
            timestamp
            level
            trace
            message
        }
    }
`