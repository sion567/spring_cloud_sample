const baseUrl = '/api'

async function request(url, options = {}) {
  const res = await fetch(baseUrl + url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers
    },
    body: options.body ? JSON.stringify(options.body) : undefined
  })
  const data = await res.json()
  if (data.code !== 200) {
    throw new Error(data.message)
  }
  return data
}

export const userApi = {
  register: (data) => request('/user/register', { method: 'POST', body: data }),
  login: (data) => request('/user/login', { method: 'POST', body: data }),
  getUser: (id) => request(`/user/${id}`),
  getAddresses: (userId) => request(`/user/${userId}/address`),
  addAddress: (userId, data) => request(`/user/${userId}/address`, { method: 'POST', body: data })
}

export const productApi = {
  list: (params) => {
    const qs = new URLSearchParams(params).toString()
    return request(`/product?${qs}`)
  },
  get: (id) => request(`/product/${id}`),
  batch: (ids) => request(`/product/batch?ids=${ids.join(',')}`)
}

export const orderApi = {
  create: (data) => request('/order', { method: 'POST', body: data }),
  get: (id) => request(`/order/${id}`),
  list: (userId, params) => {
    const qs = new URLSearchParams(params).toString()
    return request(`/order/user/${userId}?${qs}`)
  },
  pay: (id, data) => request(`/order/${id}/pay`, { method: 'PUT', body: data }),
  cancel: (id) => request(`/order/${id}/cancel`, { method: 'PUT' })
}

export default { userApi, productApi, orderApi }