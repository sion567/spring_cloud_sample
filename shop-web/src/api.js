const baseUrl = '/api'
const TOKEN_KEY = 'token'
const USER_KEY = 'user'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function getUser() {
  return JSON.parse(localStorage.getItem(USER_KEY) || '{}')
}

export function setUser(user) {
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}

async function request(url, options = {}) {
  const token = getToken()
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers
  }
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  try {
    const fetchOptions = {
      ...options,
      headers,
      body: options.body ? JSON.stringify(options.body) : undefined
    }
    const res = await fetch(baseUrl + url, fetchOptions)
    const data = await res.json()
    if (res.status !== 200 && res.status !== 201 && !data.code && !data.status) {
      throw new Error(data.detail || data.title || 'Request failed')
    }
    if (data.code && data.code !== 200) {
      throw new Error(data.message)
    }
    if (data.status && data.status !== 200) {
      throw new Error(data.detail || 'Request failed')
    }
    return data
  } catch (e) {
    if (e.message === 'Failed to fetch' || e.name === 'TypeError') {
      throw new Error('网络连接失败，请检查服务器是否启动')
    }
    throw e
  }
}

export const userApi = {
  register: (data) => request('/user/register', { method: 'POST', body: data }),
  login: (data) => {
    return request('/user/login', { method: 'POST', body: data }).then(res => {
      if (res.data?.token) {
        setToken(res.data.token)
        setUser(res.data.user)
      }
      return res
    })
  },
  refreshToken: () => {
    const token = getToken()
    if (!token) {
      return Promise.reject(new Error('No token available'))
    }
    return request('/user/refresh', {
      method: 'POST',
      body: { token }
    }).then(res => {
      if (res.data?.token) {
        setToken(res.data.token)
      }
      return res
    })
  },
  getUser: (id) => request(`/user/${id}`),
  getAddresses: (userId) => request(`/user/${userId}/address`),
  addAddress: (userId, data) => request(`/user/${userId}/address`, { method: 'POST', body: data }),
  logout: () => removeToken()
}

export const productApi = {
  list: (params) => {
    const qs = new URLSearchParams(params).toString()
    return request(`/product/list?${qs}`)
  },
  get: (id) => request(`/product/${id}`),
  batch: (ids) => request(`/product/batch?ids=${ids.join(',')}`)
}

export const orderApi = {
  create: (data) => request('/order/create', { method: 'POST', body: data }),
  get: (id) => request(`/order/${id}`),
  list: (userId, params) => {
    const qs = new URLSearchParams(params).toString()
    return request(`/order/user/${userId}?${qs}`)
  },
  pay: (id, data) => request(`/order/${id}/pay`, { method: 'PUT', body: data }),
  cancel: (id) => request(`/order/${id}/cancel`, { method: 'PUT' })
}

export default { userApi, productApi, orderApi, getToken, setToken, removeToken, getUser, setUser }
