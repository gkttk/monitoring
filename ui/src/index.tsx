import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import {ApolloClient, ApolloProvider, createHttpLink, DefaultOptions, InMemoryCache} from '@apollo/client';
import {createAuthenticationHeader} from "./util/utils";
import {setContext} from "@apollo/client/link/context";
import {loadErrorMessages} from "@apollo/client/dev";


const httpLink = createHttpLink({
    uri: '/api/graphql',
});


const authLink = setContext((_, {headers}) => {
    const authHeader = createAuthenticationHeader();
    return {
        headers: {
            ...headers, authorization: `${authHeader}`
        }
    };
});

const defaultOptions: DefaultOptions = {
    query: {
        fetchPolicy: 'no-cache', errorPolicy: 'all',
    },
}

const client = new ApolloClient({
    link: authLink.concat(httpLink), cache: new InMemoryCache(), defaultOptions: defaultOptions
});

loadErrorMessages();

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
root.render(<ApolloProvider client={client}>
    <App/>
</ApolloProvider>);
