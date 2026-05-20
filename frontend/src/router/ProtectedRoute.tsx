import { Navigate } from "react-router-dom";

type ProtectedRouteProps = {
    children: React.ReactNode;
    allowedRoles: string[];
};

export default function ProtectedRoute({ children, allowedRoles }: ProtectedRouteProps) {
    const token = localStorage.getItem("accessToken");
    const role = localStorage.getItem("role");

    if (!token) {
        return <Navigate to="/" replace />;
    }

    if (!role || !allowedRoles.includes(role)) {
        return <Navigate to="/" replace />;
    }

    return children;
}