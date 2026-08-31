import { NextResponse } from 'next/server';
import { db } from '@/lib/firebase';
import { ref, get, set } from 'firebase/database';

export async function GET() {
  try {
    if (!process.env.NEXT_PUBLIC_FIREBASE_DATABASE_URL) {
      // Return mock data if Firebase is not configured yet
      return NextResponse.json({
        isActive: true,
        title: "Promo Ramadhan!",
        imageUrl: "https://placehold.co/600x400/F2C94C/3E2723?text=Promo+Spesial",
        targetUrl: "https://example.com"
      });
    }

    const adRef = ref(db, 'ads/current');
    const snapshot = await get(adRef);
    if (snapshot.exists()) {
      return NextResponse.json(snapshot.val());
    } else {
      return NextResponse.json({ isActive: false });
    }
  } catch (error) {
    console.error(error);
    return NextResponse.json({ error: 'Failed to fetch ad' }, { status: 500 });
  }
}

export async function POST(request: Request) {
  try {
    const data = await request.json();
    
    if (!process.env.NEXT_PUBLIC_FIREBASE_DATABASE_URL) {
      return NextResponse.json({ success: true, message: "Mock saved (Firebase not configured)" });
    }

    const adRef = ref(db, 'ads/current');
    await set(adRef, data);
    
    return NextResponse.json({ success: true });
  } catch (error) {
    console.error(error);
    return NextResponse.json({ error: 'Failed to save ad' }, { status: 500 });
  }
}
