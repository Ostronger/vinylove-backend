import { createBrowserRouter } from "react-router-dom";

import LoginPage from "../pages/auth/LoginPage";
import AdminDashboardPage from "../pages/admin/AdminDashboardPage";
import InvitationTablesPage from "../pages/admin/InvitationTablesPage";
import StaffDashboardPage from "../pages/staff/StaffDashboardPage";
import AdminEventsPage from "../pages/admin/AdminEventsPage";
import ProtectedRoute from "./ProtectedRoute";
import AdminLayout from "../layouts/AdminLayout";

const router = createBrowserRouter([
    {
        path: "/",
        element: <LoginPage />
    },
    {
        path: "/admin",
        element: (<ProtectedRoute allowedRoles={["ADMIN"]}><AdminLayout /></ProtectedRoute>
        ),
        children: [
            {
                index: true,
                element: <AdminDashboardPage />
            },
            {
                path: "events",
                element: <AdminEventsPage />
            },
            {
                path: "events/:eventId/invitation-tables",
                element: <InvitationTablesPage />
            }
        ]
    },
    {
        path: "/staff",
        element: <ProtectedRoute allowedRoles={["STAFF"]}><StaffDashboardPage /></ProtectedRoute>
    }
]);

export default router;