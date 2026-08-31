'use client';
import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';

export default function AdminDashboard() {
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [adData, setAdData] = useState({
    isActive: false,
    title: '',
    imageUrl: '',
    targetUrl: ''
  });

  useEffect(() => {
    // Basic auth check
    const token = localStorage.getItem('admin_token');
    if (!token) {
      router.push('/login');
      return;
    }

    // Fetch current ad settings
    fetch('/api/ads')
      .then(res => res.json())
      .then(data => {
        if (data && typeof data.isActive !== 'undefined') {
          setAdData({
            isActive: data.isActive || false,
            title: data.title || '',
            imageUrl: data.imageUrl || '',
            targetUrl: data.targetUrl || ''
          });
        }
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, [router]);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    
    try {
      const res = await fetch('/api/ads', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(adData)
      });
      
      if (res.ok) {
        alert('Pengaturan iklan berhasil disimpan!');
      } else {
        alert('Gagal menyimpan iklan.');
      }
    } catch (err) {
      console.error(err);
      alert('Terjadi kesalahan jaringan.');
    }
    setSaving(false);
  };

  const handleLogout = () => {
    localStorage.removeItem('admin_token');
    router.push('/login');
  };

  if (loading) return <div className="p-10 text-center">Loading...</div>;

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-3xl mx-auto bg-white rounded-2xl shadow p-8">
        <div className="flex justify-between items-center mb-8 border-b pb-4">
          <h1 className="text-2xl font-bold text-gray-800">Manajemen Iklan (Popup)</h1>
          <button 
            onClick={handleLogout}
            className="text-red-500 hover:text-red-700 font-medium"
          >
            Logout
          </button>
        </div>

        <form onSubmit={handleSave} className="space-y-6">
          <div className="flex items-center p-4 bg-gray-100 rounded-xl">
            <input
              type="checkbox"
              id="isActive"
              checked={adData.isActive}
              onChange={(e) => setAdData({...adData, isActive: e.target.checked})}
              className="w-6 h-6 text-yellow-500 rounded border-gray-300 focus:ring-yellow-500"
            />
            <label htmlFor="isActive" className="ml-3 text-lg font-medium text-gray-800">
              Aktifkan Iklan Popup
            </label>
          </div>

          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Judul Iklan</label>
              <input
                type="text"
                value={adData.title}
                onChange={(e) => setAdData({...adData, title: e.target.value})}
                placeholder="Contoh: Promo Ramadhan"
                className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-yellow-500 outline-none text-black"
                required={adData.isActive}
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">URL Gambar (Image Link)</label>
              <input
                type="url"
                value={adData.imageUrl}
                onChange={(e) => setAdData({...adData, imageUrl: e.target.value})}
                placeholder="https://example.com/image.png"
                className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-yellow-500 outline-none text-black"
                required={adData.isActive}
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">URL Tujuan (Saat Diklik)</label>
              <input
                type="url"
                value={adData.targetUrl}
                onChange={(e) => setAdData({...adData, targetUrl: e.target.value})}
                placeholder="https://tarbiyah.com/promo"
                className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-yellow-500 outline-none text-black"
                required={adData.isActive}
              />
            </div>
          </div>

          {adData.imageUrl && (
            <div className="mt-6 border rounded-xl p-4">
              <p className="text-sm text-gray-500 mb-2">Preview Gambar:</p>
              <img 
                src={adData.imageUrl} 
                alt="Preview" 
                className="max-h-60 rounded-lg object-contain bg-gray-100 w-full"
                onError={(e) => (e.currentTarget.src = 'https://placehold.co/600x400/eeeeee/999999?text=Image+Not+Found')}
              />
            </div>
          )}

          <button
            type="submit"
            disabled={saving}
            className="w-full bg-yellow-500 text-white font-bold py-3 rounded-xl hover:bg-yellow-600 transition-colors disabled:opacity-50"
          >
            {saving ? 'Menyimpan...' : 'Simpan Pengaturan Iklan'}
          </button>
        </form>
      </div>
    </div>
  );
}
