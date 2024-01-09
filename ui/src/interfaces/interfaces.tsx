export interface UserType {
    id: string,
    login: string
}

export interface LoggedUserType {
    login: string,
    admin: boolean
}

export interface UserDataType {
    login: string,
    admin: boolean,
    token: string
}

export interface NotificationType {
    id: string
    componentName: string
    description: string
    severity: string
    timestamp: string
}

export interface ConfigurationType {
    id: string
    key: string
    value: string
}

export interface FilterType {
    componentId?: string,
    severity?: string
}

export interface AuthRequest {
    login: string
    password: string
}