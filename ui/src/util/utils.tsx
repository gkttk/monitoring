import {FilterType, UserDataType} from "../interfaces/interfaces";
import {ApolloClient, DocumentNode} from "@apollo/client";

export function createAuthenticationHeader() {
    const userDataValue = localStorage.getItem("user_data")
    if (userDataValue) {
        const userData: UserDataType = JSON.parse(userDataValue)
        return `Bearer ${userData.token}`;
    }
    return "";


}


export function prepareFilter(filter: FilterType) {
    let preparedFilter: FilterType = filter;
    if (filter.componentId === "ANY") {
        const {componentId, ...rest} = preparedFilter;
        preparedFilter = rest;
    }

    if (filter.severity === "ANY") {
        const {severity, ...rest} = preparedFilter;
        preparedFilter = rest;
    }

    return preparedFilter;
}

export async function sendMutate(client: ApolloClient<any>, mutation: DocumentNode, variables: Object) {

    const {data, errors} = await client.mutate({
        mutation: mutation, variables: variables,
    })

    return {data, errors};
}

export async function sendQuery(client: ApolloClient<any>, query: DocumentNode, variables?: Object) {

    const {data, errors} = await client.query({
        query: query, variables: variables,
    })

    return {data, errors};
}

export function validateEmail(email: string) {
    let re = /\S+@\S+\.\S+/;
    return re.test(email);
}

export function isProvidedSeverityValidEnum(value: string) {
    return "INFO" === value || "WARNING" === value || "ERROR" === value
}