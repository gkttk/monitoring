import {gql} from "@apollo/client";

export const USERS_QUERY = gql`
    {
        users {
            id
            login
        }
    }
`;

export const USER_BY_ID_QUERY = gql`
    query UserByID($id: ID!) {
        userById(id: $id) {
            id
            login
        }
    }
`;

export const NOTIFICATIONS_QUERY = gql`
    query NotificationsByFilter($filter: NotificationFilter) {
        notifications(filter: $filter) {
            id
            componentName
            description
            severity
            timestamp
        }
    }
`;

export const CREATE_NOTIFICATION_MUTATION = gql`
    mutation CreateNotification($notification: NotificationRequest) {
        createNotification(notification: $notification) {
            id
            componentName
            description
            severity
            timestamp
        }
    }
`;

export const CONFIGS_QUERY = gql`
    {
        configurations {
            id
            key
            value
        }
    }
`;


export const UPDATE_CONFIGURATION_MUTATION = gql`
    mutation UpdateConfiguration($id: ID!, $newValue: String!) {
        updateConfiguration(id: $id, newValue: $newValue) {
            id
            key
            value
        }
    }
`;

export const VALIDATE_TOKEN_QUERY = gql`
    query validate($data: InputAuthData) {
        validate(data: $data){
            login
            admin
            token
        }
    }
`;

export const LOG_IN = gql`
    query auth($authRequest: AuthRequest) {
        auth(authRequest: $authRequest){
            login
            admin
            token
        }
    }
`;