import { useState, useEffect } from 'react'
import Navbar from '../components/Navbar'
import api from '../api/axios'

function StatusBadge({ status }) {
  return <span className={`badge badge-${status.toLowerCase()}`}>{status}</span>
}

export default function DoctorDashboard() {
  const [appointments, setAppointments] = useState([])
  const [filter, setFilter] = useState('ALL')
  const [loading, setLoading] = useState(true)
  const [msg, setMsg] = useState('')

  useEffect(() => { loadAppointments() }, [])

  const loadAppointments = async () => {
    try {
      const { data } = await api.get('/appointments/my')
      setAppointments(data)
    } catch {}
    setLoading(false)
  }

  const action = async (method, url, successMsg) => {
    try {
      await api.put(url)
      setMsg('✅ ' + successMsg)
      loadAppointments()
    } catch (err) {
      setMsg('❌ ' + (err.response?.data?.message || 'Action failed'))
    }
  }

  const filtered = filter === 'ALL' ? appointments
    : appointments.filter(a => a.status === filter)

  const counts = appointments.reduce((acc, a) => {
    acc[a.status] = (acc[a.status] || 0) + 1; return acc
  }, {})

  return (
    <>
      <Navbar />
      <div className="dashboard container">
        <h1 className="page-title">Doctor Dashboard</h1>

        {/* Stats */}
        <div className="stats-grid">
          {[
            { label: 'Total', value: appointments.length, color: '#1a5276' },
            { label: 'Pending', value: counts.PENDING || 0, color: '#f39c12' },
            { label: 'Confirmed', value: counts.CONFIRMED || 0, color: '#27ae60' },
            { label: 'Completed', value: counts.COMPLETED || 0, color: '#2980b9' },
            { label: 'Cancelled', value: counts.CANCELLED || 0, color: '#e74c3c' },
          ].map(s => (
            <div key={s.label} className="stat-card">
              <div className="number" style={{ color: s.color }}>{s.value}</div>
              <div className="label">{s.label}</div>
            </div>
          ))}
        </div>

        {msg && <div className={`alert ${msg.startsWith('✅') ? 'alert-success' : 'alert-error'}`}
          style={{ marginBottom: '1rem' }}>{msg}</div>}

        {/* Filter Tabs */}
        <div className="tabs">
          {['ALL', 'PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED'].map(f => (
            <button key={f} className={`tab ${filter === f ? 'active' : ''}`} onClick={() => setFilter(f)}>
              {f} {f !== 'ALL' && counts[f] ? `(${counts[f]})` : ''}
            </button>
          ))}
        </div>

        {loading ? (
          <div className="loading"><div className="spinner"></div></div>
        ) : (
          <div className="appt-list">
            {filtered.length === 0 && <p style={{ color: '#718096' }}>No appointments in this category.</p>}
            {filtered.map(a => (
              <div key={a.id} className={`appt-item ${a.status}`}>
                <div className="appt-info">
                  <h4>👤 {a.patientName}</h4>
                  <p>📅 {a.appointmentDate} at {a.appointmentTime?.substring(0, 5)}</p>
                  <p>📧 {a.patientEmail}</p>
                  {a.patientNotes && <p>📝 Patient notes: {a.patientNotes}</p>}
                </div>
                <div className="appt-actions">
                  <StatusBadge status={a.status} />
                  {a.status === 'PENDING' && <>
                    <button className="btn btn-success btn-sm"
                      onClick={() => action('put', `/appointments/confirm/${a.id}`, 'Appointment confirmed!')}>
                      ✓ Confirm
                    </button>
                    <button className="btn btn-danger btn-sm"
                      onClick={() => action('put', `/appointments/reject/${a.id}`, 'Appointment rejected.')}>
                      ✗ Reject
                    </button>
                  </>}
                  {a.status === 'CONFIRMED' && <>
                    <button className="btn btn-info btn-sm"
                      onClick={() => action('put', `/appointments/complete/${a.id}`, 'Appointment marked complete!')}>
                      ✓ Complete
                    </button>
                    <button className="btn btn-danger btn-sm"
                      onClick={() => action('put', `/appointments/cancel/${a.id}`, 'Appointment cancelled.')}>
                      Cancel
                    </button>
                  </>}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </>
  )
}
