import { createBrowserRouter } from "react-router-dom";

import LoginPage from "../pages/auth/LoginPage";
import AdminDashboardPage from "../pages/admin/AdminDashboardPage";
import InvitationTablesPage from "../pages/admin/InvitationTablesPage";
import StaffDashboardPage from "../pages/staff/StaffDashboardPage";
import AdminEventsPage from "../pages/admin/AdminEventsPage";
import AdminScanPage from "../pages/admin/AdminScanPage";
import ProtectedRoute from "./ProtectedRoute";
import AdminLayout from "../layouts/AdminLayout";
import StaffLayout from "../layouts/StaffLayout";
import StaffScanPage from "../pages/staff/StaffScanPage";
import StaffEventsPage from "../pages/staff/StaffEventsPage";
 
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
            },
            {
                path: "scan",
                element: <AdminScanPage />
            }
        ]
    },
    {
        path: "/staff",
        element: (<ProtectedRoute allowedRoles={["STAFF", "ADMIN"]}><StaffLayout /></ProtectedRoute>
        ),
        children: [
            {
                index: true,
                element: <StaffDashboardPage />
            },
            {
                path: "scan",
                element: <StaffScanPage />
            },
            {
                path: "events",
                element: <StaffEventsPage />
            }
        ]
    }
]);

export default router;