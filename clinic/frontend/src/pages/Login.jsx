import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'

export default function Login() {
  const [form, setForm] = useState({ email: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      console.log('🔐 Logging in with:', form.email)
      const { data } = await api.post('/auth/login', form)
      console.log('✅ Login response:', data)
      login(data)
      if (data.role === 'ADMIN') navigate('/admin')
      else if (data.role === 'DOCTOR') navigate('/doctor')
      else navigate('/patient')
    } catch (err) {
      console.error('❌ Login error:', err)
      const errorMsg = err.response?.data?.message || err.response?.data?.error || err.message || 'Login failed. Check your credentials.'
      setError(errorMsg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h2>🏥 Clinic Booking</h2>
        <p className="subtitle">Sign in to your account</p>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email</label>
            <input type="email" placeholder="you@example.com" value={form.email}
              onChange={e => setForm({...form, email: e.target.value})} required />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input type="password" placeholder="••••••" value={form.password}
              onChange={e => setForm({...form, password: e.target.value})} required />
          </div>
          <button className="btn btn-primary" type="submit" disabled={loading}>
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>

        <div style={{ marginTop: '1.2rem', textAlign: 'center' }}>
          <span className="link" onClick={() => navigate('/register')}>
            Don't have an account? Register
          </span>
        </div>

        <div style={{ marginTop: '1.5rem', padding: '1rem', background: '#f7fafc', borderRadius: '6px', fontSize: '0.8rem', color: '#718096' }}>
          <strong>Default accounts:</strong><br/>
          Admin: admin@clinic.com / admin123<br/>
          Doctor: dr.ahmed@clinic.com / doctor123<br/>
          Patient: patient@clinic.com / patient123
        </div>
      </div>
    </div>
  )
}
