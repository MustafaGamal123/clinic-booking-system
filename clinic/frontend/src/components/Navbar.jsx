import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const dashboardPath = user?.role === 'ADMIN' ? '/admin'
    : user?.role === 'DOCTOR' ? '/doctor' : '/patient'

  return (
    <div className="navbar">
      <div className="container inner">
        <div className="logo">🏥 Clinic Booking</div>
        <div className="nav-links">
          <span style={{ fontSize: '0.85rem' }}>
            👤 {user?.firstName} {user?.lastName} ({user?.role})
          </span>
          <a onClick={() => navigate(dashboardPath)} style={{ cursor: 'pointer' }}>Dashboard</a>
          <button className="btn-logout" onClick={handleLogout}>Logout</button>
        </div>
      </div>
    </div>
  )
}
