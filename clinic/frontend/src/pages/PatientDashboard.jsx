import { useState, useEffect } from 'react'
import Navbar from '../components/Navbar'
import api from '../api/axios'

const TIMES = ['09:00', '09:30', '10:00', '10:30', '11:00', '11:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30']

function StatusBadge({ status }) {
  return <span className={`badge badge-${status.toLowerCase()}`}>{status}</span>
}

export default function PatientDashboard() {
  const [tab, setTab] = useState('doctors')
  const [doctors, setDoctors] = useState([])
  const [appointments, setAppointments] = useState([])
  const [loading, setLoading] = useState(true)
  const [booking, setBooking] = useState(null) // doctor being booked
  const [bookForm, setBookForm] = useState({ appointmentDate: '', appointmentTime: '', patientNotes: '' })
  const [msg, setMsg] = useState('')
  const [search, setSearch] = useState('')

  useEffect(() => { loadData() }, [])

  const loadData = async () => {
    setLoading(true)
    try {
      const [d, a] = await Promise.all([
        api.get('/doctors/available'),
        api.get('/appointments/my')
      ])
      setDoctors(d.data)
      setAppointments(a.data)
    } catch {}
    setLoading(false)
  }

  const filteredDoctors = doctors.filter(d =>
    d.specialty?.toLowerCase().includes(search.toLowerCase()) ||
    `${d.firstName} ${d.lastName}`.toLowerCase().includes(search.toLowerCase())
  )

  const openBooking = (doctor) => {
    setBooking(doctor)
    setBookForm({ appointmentDate: '', appointmentTime: '', patientNotes: '' })
    setMsg('')
  }

  const submitBooking = async (e) => {
    e.preventDefault()
    try {
      await api.post('/appointments/book', {
        doctorId: booking.id,
        appointmentDate: bookForm.appointmentDate,
        appointmentTime: bookForm.appointmentTime + ':00',
        patientNotes: bookForm.patientNotes
      })
      setMsg('✅ Appointment booked successfully!')
      setBooking(null)
      loadData()
      setTab('appointments')
    } catch (err) {
      setMsg('❌ ' + (err.response?.data?.message || 'Booking failed'))
    }
  }

  const cancelAppointment = async (id) => {
    if (!confirm('Cancel this appointment?')) return
    try {
      await api.put(`/appointments/cancel/${id}`)
      loadData()
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to cancel')
    }
  }

  const minDate = new Date().toISOString().split('T')[0]

  return (
    <>
      <Navbar />
      <div className="dashboard container">
        <h1 className="page-title">Patient Dashboard</h1>

        {msg && <div className={`alert ${msg.startsWith('✅') ? 'alert-success' : 'alert-error'}`}>{msg}</div>}

        <div className="tabs">
          <button className={`tab ${tab === 'doctors' ? 'active' : ''}`} onClick={() => setTab('doctors')}>Find Doctors</button>
          <button className={`tab ${tab === 'appointments' ? 'active' : ''}`} onClick={() => setTab('appointments')}>
            My Appointments ({appointments.length})
          </button>
        </div>

        {loading ? (
          <div className="loading"><div className="spinner"></div><p>Loading...</p></div>
        ) : tab === 'doctors' ? (
          <>
            <div className="form-group" style={{ maxWidth: 360, marginBottom: '1.2rem' }}>
              <input placeholder="🔍 Search by name or specialty..."
                value={search} onChange={e => setSearch(e.target.value)} />
            </div>
            <div className="doctors-grid">
              {filteredDoctors.map(d => (
                <div key={d.id} className="doctor-card">
                  <h3>Dr. {d.firstName} {d.lastName}</h3>
                  <div className="specialty">🏥 {d.specialty}</div>
                  {d.bio && <p className="info">{d.bio.substring(0, 80)}...</p>}
                  <p className="info">⏰ {d.workingHours} | 📅 {d.workingDays?.replace(/,/g, ', ')}</p>
                  <p className="info">🎓 {d.experienceYears} years experience</p>
                  <div className="fee">💰 {d.consultationFee} EGP</div>
                  <button className="btn btn-primary" onClick={() => openBooking(d)}>Book Appointment</button>
                </div>
              ))}
              {filteredDoctors.length === 0 && <p style={{ color: '#718096' }}>No doctors found.</p>}
            </div>
          </>
        ) : (
          <div className="appt-list">
            {appointments.length === 0 && <p style={{ color: '#718096' }}>No appointments yet. Go book one!</p>}
            {appointments.map(a => (
              <div key={a.id} className={`appt-item ${a.status}`}>
                <div className="appt-info">
                  <h4>Dr. {a.doctorName} — {a.specialty}</h4>
                  <p>📅 {a.appointmentDate} at {a.appointmentTime?.substring(0, 5)}</p>
                  {a.patientNotes && <p>📝 {a.patientNotes}</p>}
                  {a.doctorNotes && <p style={{ color: '#2980b9' }}>Doctor: {a.doctorNotes}</p>}
                </div>
                <div className="appt-actions">
                  <StatusBadge status={a.status} />
                  {(a.status === 'PENDING' || a.status === 'CONFIRMED') && (
                    <button className="btn btn-danger btn-sm" onClick={() => cancelAppointment(a.id)}>Cancel</button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Booking Modal */}
      {booking && (
        <div className="modal-overlay" onClick={() => setBooking(null)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h3>Book with Dr. {booking.firstName} {booking.lastName}</h3>
            <p style={{ color: '#2980b9', marginBottom: '1rem', fontSize: '0.9rem' }}>
              {booking.specialty} • {booking.consultationFee} EGP
            </p>
            {msg && <div className="alert alert-error">{msg}</div>}
            <form onSubmit={submitBooking}>
              <div className="form-group">
                <label>Date</label>
                <input type="date" min={minDate} value={bookForm.appointmentDate}
                  onChange={e => setBookForm({...bookForm, appointmentDate: e.target.value})} required />
              </div>
              <div className="form-group">
                <label>Time</label>
                <select value={bookForm.appointmentTime}
                  onChange={e => setBookForm({...bookForm, appointmentTime: e.target.value})} required>
                  <option value="">Select time</option>
                  {TIMES.map(t => <option key={t} value={t}>{t}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label>Notes (optional)</label>
                <textarea rows={2} value={bookForm.patientNotes}
                  onChange={e => setBookForm({...bookForm, patientNotes: e.target.value})}
                  style={{ width: '100%', padding: '0.65rem', border: '1.5px solid #cbd5e0', borderRadius: '6px' }}
                  placeholder="Describe your symptoms..." />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn" onClick={() => setBooking(null)}
                  style={{ background: '#e2e8f0' }}>Cancel</button>
                <button type="submit" className="btn btn-success">Confirm Booking</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  )
}
