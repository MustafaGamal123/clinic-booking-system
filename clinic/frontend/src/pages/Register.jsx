import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'

export default function Register() {
  const [form, setForm] = useState({
    firstName: '', lastName: '', email: '', password: '', phone: '',
    role: 'PATIENT', specialty: '', bio: '', experienceYears: '', consultationFee: '',
    workingDays: 'MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY', workingHours: '09:00-17:00'
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const set = (field) => (e) => setForm({ ...form, [field]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const payload = {
        ...form,
        experienceYears: form.experienceYears ? parseInt(form.experienceYears) : undefined,
        consultationFee: form.consultationFee ? parseFloat(form.consultationFee) : undefined,
      }
      const { data } = await api.post('/auth/register', payload)
      login(data)
      navigate(data.role === 'DOCTOR' ? '/doctor' : '/patient')
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-card" style={{ maxWidth: 520 }}>
        <h2>Create Account</h2>
        <p className="subtitle">Join our clinic booking system</p>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label>First Name</label>
              <input value={form.firstName} onChange={set('firstName')} required />
            </div>
            <div className="form-group">
              <label>Last Name</label>
              <input value={form.lastName} onChange={set('lastName')} required />
            </div>
          </div>

          <div className="form-group">
            <label>Email</label>
            <input type="email" value={form.email} onChange={set('email')} required />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Password</label>
              <input type="password" value={form.password} onChange={set('password')} required minLength={6} />
            </div>
            <div className="form-group">
              <label>Phone</label>
              <input value={form.phone} onChange={set('phone')} placeholder="01xxxxxxxxx" />
            </div>
          </div>

          <div className="form-group">
            <label>Register as</label>
            <select value={form.role} onChange={set('role')}>
              <option value="PATIENT">Patient</option>
              <option value="DOCTOR">Doctor</option>
            </select>
          </div>

          {form.role === 'DOCTOR' && (
            <>
              <div className="form-row">
                <div className="form-group">
                  <label>Specialty *</label>
                  <input value={form.specialty} onChange={set('specialty')} placeholder="e.g. Cardiology" required />
                </div>
                <div className="form-group">
                  <label>Experience (years)</label>
                  <input type="number" value={form.experienceYears} onChange={set('experienceYears')} min="0" />
                </div>
              </div>
              <div className="form-group">
                <label>Bio</label>
                <textarea rows={3} value={form.bio} onChange={set('bio')} style={{ width: '100%', padding: '0.65rem', border: '1.5px solid #cbd5e0', borderRadius: '6px' }} />
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Consultation Fee (EGP)</label>
                  <input type="number" value={form.consultationFee} onChange={set('consultationFee')} min="0" />
                </div>
                <div className="form-group">
                  <label>Working Hours</label>
                  <input value={form.workingHours} onChange={set('workingHours')} placeholder="09:00-17:00" />
                </div>
              </div>
            </>
          )}

          <button className="btn btn-primary" type="submit" disabled={loading} style={{ marginTop: '0.5rem' }}>
            {loading ? 'Creating account...' : 'Create Account'}
          </button>
        </form>

        <div style={{ marginTop: '1rem', textAlign: 'center' }}>
          <span className="link" onClick={() => navigate('/login')}>Already have an account? Sign in</span>
        </div>
      </div>
    </div>
  )
}
