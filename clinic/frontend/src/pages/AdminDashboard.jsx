import { useState, useEffect } from 'react'
import Navbar from '../components/Navbar'
import api from '../api/axios'

function StatusBadge({ status }) {
  return <span className={`badge badge-${status.toLowerCase()}`}>{status}</span>
}

export default function AdminDashboard() {
  const [tab, setTab] = useState('stats')
  const [stats, setStats] = useState(null)
  const [doctors, setDoctors] = useState([])
  const [appointments, setAppointments] = useState([])
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => { loadData() }, [])

  const loadData = async () => {
    setLoading(true)
    try {
      const [s, d, a, u] = await Promise.all([
        api.get('/admin/stats'),
        api.get('/admin/doctors'),
        api.get('/admin/appointments'),
        api.get('/admin/users'),
      ])
      setStats(s.data); setDoctors(d.data); setAppointments(a.data); setUsers(u.data)
    } catch {}
    setLoading(false)
  }

  const toggleDoctor = async (doctorId) => {
    await api.put(`/admin/doctors/${doctorId}/toggle-availability`)
    loadData()
  }

  const toggleUser = async (userId) => {
    await api.put(`/admin/users/${userId}/toggle-active`)
    loadData()
  }

  return (
    <>
      <Navbar />
      <div className="dashboard container">
        <h1 className="page-title">Admin Dashboard</h1>

        <div className="tabs">
          {['stats', 'doctors', 'appointments', 'users'].map(t => (
            <button key={t} className={`tab ${tab === t ? 'active' : ''}`} onClick={() => setTab(t)}>
              {t.charAt(0).toUpperCase() + t.slice(1)}
            </button>
          ))}
        </div>

        {loading ? (
          <div className="loading"><div className="spinner"></div><p>Loading...</p></div>
        ) : tab === 'stats' ? (
          <div className="stats-grid" style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))' }}>
            {stats && [
              { label: 'Total Patients', value: stats.totalPatients, color: '#27ae60' },
              { label: 'Total Doctors', value: stats.totalDoctors, color: '#1a5276' },
              { label: 'Total Appointments', value: stats.totalAppointments, color: '#2980b9' },
              { label: 'Pending', value: stats.pendingAppointments, color: '#f39c12' },
              { label: 'Confirmed', value: stats.confirmedAppointments, color: '#27ae60' },
              { label: 'Completed', value: stats.completedAppointments, color: '#2980b9' },
              { label: 'Cancelled', value: stats.cancelledAppointments, color: '#e74c3c' },
            ].map(s => (
              <div key={s.label} className="stat-card">
                <div className="number" style={{ color: s.color }}>{s.value}</div>
                <div className="label">{s.label}</div>
              </div>
            ))}
          </div>
        ) : tab === 'doctors' ? (
          <div className="card">
            <div className="card-header">All Doctors ({doctors.length})</div>
            <div className="card-body" style={{ padding: 0 }}>
              <table className="table">
                <thead><tr>
                  <th>Name</th><th>Specialty</th><th>Experience</th><th>Fee</th><th>Status</th><th>Action</th>
                </tr></thead>
                <tbody>
                  {doctors.map(d => (
                    <tr key={d.id}>
                      <td>Dr. {d.firstName} {d.lastName}<br/><small style={{color:'#718096'}}>{d.email}</small></td>
                      <td>{d.specialty}</td>
                      <td>{d.experienceYears} yrs</td>
                      <td>{d.consultationFee} EGP</td>
                      <td><span className={`badge ${d.available ? 'badge-confirmed' : 'badge-cancelled'}`}>
                        {d.available ? 'Available' : 'Unavailable'}</span></td>
                      <td>
                        <button className={`btn btn-sm ${d.available ? 'btn-warning' : 'btn-success'}`}
                          onClick={() => toggleDoctor(d.id)}>
                          {d.available ? 'Disable' : 'Enable'}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        ) : tab === 'appointments' ? (
          <div className="card">
            <div className="card-header">All Appointments ({appointments.length})</div>
            <div className="card-body" style={{ padding: 0 }}>
              <table className="table">
                <thead><tr>
                  <th>Patient</th><th>Doctor</th><th>Date & Time</th><th>Status</th>
                </tr></thead>
                <tbody>
                  {appointments.map(a => (
                    <tr key={a.id}>
                      <td>{a.patientName}</td>
                      <td>Dr. {a.doctorName}<br/><small style={{color:'#718096'}}>{a.specialty}</small></td>
                      <td>{a.appointmentDate}<br/><small>{a.appointmentTime?.substring(0,5)}</small></td>
                      <td><StatusBadge status={a.status} /></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        ) : (
          <div className="card">
            <div className="card-header">All Users ({users.length})</div>
            <div className="card-body" style={{ padding: 0 }}>
              <table className="table">
                <thead><tr>
                  <th>Name</th><th>Email</th><th>Role</th><th>Status</th><th>Action</th>
                </tr></thead>
                <tbody>
                  {users.map(u => (
                    <tr key={u.id}>
                      <td>{u.firstName} {u.lastName}</td>
                      <td>{u.email}</td>
                      <td><span className={`badge badge-${u.role === 'ADMIN' ? 'pending' : u.role === 'DOCTOR' ? 'confirmed' : 'completed'}`}>{u.role}</span></td>
                      <td><span className={`badge ${u.active ? 'badge-confirmed' : 'badge-cancelled'}`}>{u.active ? 'Active' : 'Inactive'}</span></td>
                      <td>
                        {u.role !== 'ADMIN' && (
                          <button className={`btn btn-sm ${u.active ? 'btn-danger' : 'btn-success'}`}
                            onClick={() => toggleUser(u.id)}>
                            {u.active ? 'Deactivate' : 'Activate'}
                          </button>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>
    </>
  )
}
